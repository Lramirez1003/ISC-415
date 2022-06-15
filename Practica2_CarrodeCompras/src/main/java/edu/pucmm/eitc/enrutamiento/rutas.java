package edu.pucmm.eitc.enrutamiento;

import edu.pucmm.eitc.util.BaseControlador;
import edu.pucmm.eitc.services.Service;
import io.javalin.Javalin;

import edu.pucmm.eitc.encapsulaciones.*;
import io.javalin.plugin.rendering.JavalinRenderer;
import io.javalin.plugin.rendering.template.JavalinVelocity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class rutas extends BaseControlador {


    public rutas(Javalin app) {
        super(app);
    }
    private void registrandoPlantillas(){
        JavalinRenderer.register(JavalinVelocity.INSTANCE, ".vm");
    }
    @Override
    public void aplicarRutas() {

        Service service = Service.getInstance();

        app.before(ctx -> {
            CarroCompra cart = ctx.sessionAttribute("carrito");
            if(cart == null){
                cart = new CarroCompra(service.getCart());
            }
            ctx.sessionAttribute("carrito",cart);

        });

        app.get("/", ctx -> {
            CarroCompra carrito = ctx.sessionAttribute("carrito");

            List<Producto> productos = service.getProductos();
            Map<String, Object> modelo = new HashMap<>();
            modelo.put("productos",productos);
            modelo.put("cantidad",carrito.getListaProductos().size());
            ctx.render("/publico/listaProd.vm", modelo);
        });

        app.post("/comprar", ctx -> {
            CarroCompra carro = ctx.sessionAttribute("carrito");

            Producto temp = carro.getProductID(ctx.formParamAsClass("id",
                    Integer.class).get());
            if(temp == null){
                temp = service.getProductoID(ctx.formParamAsClass("id",
                        Integer.class).get());
                temp.setCantidad(ctx.formParamAsClass("cantidad",Integer.class).get() );
                carro.addProducto(temp);
                ctx.sessionAttribute("carrito",carro);
                ctx.redirect("/comprar");
            }else{
                int posicion = carro.getPosicion(ctx.formParamAsClass("id",Integer.class).get());
                temp.setCantidad(ctx.formParamAsClass("cantidad",Integer.class).get() + temp.getCantidad());
                carro.changeProducto(temp,posicion);
            }

            ctx.sessionAttribute("carrito",carro);
            ctx.redirect("/comprar");
        });

        app.get("/comprar",ctx -> {
           ctx.redirect("/");
        });

        app.get("/ventas",ctx -> {

            if(ctx.cookie("usuario" ) == null || ctx.cookie("password") == null|| !ctx.cookie("usuario").equalsIgnoreCase("admin")|| !ctx.cookie("password").equalsIgnoreCase("admin")){
                ctx.redirect("/auth/ventas");
                    return;
            }
            CarroCompra carro = ctx.sessionAttribute("carrito");
            List<VentasProductos> ventas = service.getVentas();
            Map<String,Object> modelo = new HashMap<>();
            modelo.put("ventas",ventas);
            modelo.put("cantidad",carro.getListaProductos().size());

            ctx.render("publico/ventas.vm",modelo);
        });

        app.get("/productos",ctx -> {

            if(ctx.cookie("usuario" ) == null || ctx.cookie("password") == null|| !ctx.cookie("usuario").equalsIgnoreCase("admin")|| !ctx.cookie("password").equalsIgnoreCase("admin")){
                ctx.redirect("/auth/productos");
                 return;
            }
            CarroCompra carro = ctx.sessionAttribute("carrito");
            List<Producto> producto = service.getProductos();
            Map<String,Object> modelo = new HashMap<>();
            modelo.put("productos",producto);
            modelo.put("cantidad",carro.getListaProductos().size());

            ctx.render("/publico/productos.vm",modelo);
        });

        app.get("/registrar",ctx -> {
            Map<String,Object>modelo = new HashMap<>();
            modelo.put("accion","/registrar");
            CarroCompra carro = ctx.sessionAttribute("carrito");
            modelo.put("cantidad",carro.getListaProductos().size());
            ctx.render("publico/registrar.vm",modelo);
        });

        app.post("/registrar",ctx -> {
            String nombre= ctx.formParam("nombre");
            int precio = ctx.formParamAsClass("precio",Integer.class).get();
            Producto tmp = new Producto(nombre,precio);
            service.registerProducto(tmp);
            ctx.redirect("/productos");
        });

        app.get("/edit/{id}",ctx -> {
            Producto tmp = service.getProductoID(ctx.pathParamAsClass("id",Integer.class).get());
            Map<String,Object> modelo = new HashMap<>();
            modelo.put("producto",tmp);
            modelo.put("accion","/edit/"+ctx.pathParamAsClass("id",Integer.class).get());
            CarroCompra carro = ctx.sessionAttribute("carrito");
            modelo.put("cantidad",carro.getListaProductos().size());
            ctx.render("/publico/registrar.vm",modelo);
        });

        app.post("/edit/{id}",ctx -> {
            String nombre= ctx.formParam("nombre");
            int precio = ctx.formParamAsClass("precio",Integer.class).get();

            Producto tmp = new Producto(nombre,precio);
            tmp.setId(ctx.pathParamAsClass("id",Integer.class).get());
            service.registerProducto(tmp);
            ctx.redirect("/productos");

        });

        app.get("/auth/{direc}", ctx -> {
            String direc = ctx.pathParam("direc");
            Map<String, Object> modelo = new HashMap<>();
            modelo.put("direc",direc);
            ctx.render("/publico/login.vm",modelo);
        });

        app.post("/auth/{direc}",ctx -> {
            String user = ctx.formParam("usuario");
            String pass = ctx.formParam("password");
            String tmp = ctx.formParam("direc");

            if (user == null || pass == null){
                ctx.redirect("/auth/"+tmp);
            }
            ctx.cookie("usuario", user);
            ctx.cookie("password", pass);

            ctx.redirect("/");

        });

        /*Carga el carrito pasando la lista de productos que se tiene dentro del carro*/
        app.get("/carrito", ctx -> {
            CarroCompra carro = ctx.sessionAttribute("carrito");
            if(carro == null){
                carro = new CarroCompra(service.getCart());
            }
            ctx.sessionAttribute("carrito",carro);
            Map<String, Object> modelo = new HashMap<>();
            modelo.put("productos",carro.getListaProductos());
            modelo.put("cantidad",carro.getListaProductos().size());
            ctx.render("/publico/carrito.vm",modelo);
        });
        /*Elimina un producto del carrito a partir de su id*/
        app.get("/delete/{id}", ctx -> {
            int id = ctx.pathParamAsClass("id", Integer.class).get();
            CarroCompra carro = ctx.sessionAttribute("carrito");
            carro.deleteProductID(id);

            ctx.sessionAttribute("carrito",carro);
            ctx.redirect("/carrito");
        });

        /*Procesa la compra*/
        app.post("/procesar",ctx -> {
            CarroCompra carro = ctx.sessionAttribute("carrito");
            if(carro.getListaProductos().size() < 1){
                ctx.redirect("/carrito");
            }
            String nombre = ctx.formParam("nombre");
            VentasProductos venta = new VentasProductos(service.getVentas().size()+1,nombre,carro.listaProductos);
            service.addVentas(venta);
            carro.deleteProductos();
            ctx.sessionAttribute("carrito",carro);
            ctx.redirect("/comprar");
        });



        app.get("/remove/{id}",ctx -> {
            service.deleteProducto(ctx.pathParamAsClass("id",Integer.class).get());
            ctx.redirect("/productos");

        });

        app.get("/clear",ctx -> {
            CarroCompra carro = ctx.sessionAttribute("carrito");
            carro.deleteProductos();
            ctx.redirect("/comprar");
        });





    }
}
