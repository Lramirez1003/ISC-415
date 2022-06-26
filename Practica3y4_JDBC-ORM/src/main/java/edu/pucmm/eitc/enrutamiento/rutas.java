package edu.pucmm.eitc.enrutamiento;

import edu.pucmm.eitc.util.BaseControlador;
import edu.pucmm.eitc.services.Service;
import io.javalin.Javalin;

import edu.pucmm.eitc.encapsulaciones.*;
import io.javalin.plugin.rendering.JavalinRenderer;
import io.javalin.plugin.rendering.template.JavalinVelocity;
import org.jasypt.util.password.StrongPasswordEncryptor;
import org.jasypt.util.text.AES256TextEncryptor;

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
            CarroCompra cart = ctx.sessionAttribute("carro");
            if(cart == null){
                cart = new CarroCompra(service.getCart());
            }
            ctx.sessionAttribute("carro",cart);

        });

        app.get("/", ctx -> {
            CarroCompra carro = ctx.sessionAttribute("carro");

            List<Producto> productos = service.getProductos();
            Map<String, Object> modelo = new HashMap<>();
            modelo.put("productos",productos);
            modelo.put("cantidad",carro.getListaProductos().size());
            ctx.render("/publico/listaProd.vm", modelo);
        });

        app.post("/comprar", ctx -> {
            CarroCompra carro = ctx.sessionAttribute("carro");

            Producto temp = carro.getProductID(ctx.formParamAsClass("id",
                    Integer.class).get());
            if(temp == null){
                temp = service.getProductoID(ctx.formParamAsClass("id",
                        Integer.class).get());
                temp.setCantidad(ctx.formParamAsClass("cantidad",Integer.class).get() );
                carro.addProducto(temp);
                ctx.sessionAttribute("carro",carro);
                ctx.redirect("/comprar");
            }else{
                int posicion = carro.getPosicion(ctx.formParamAsClass("id",Integer.class).get());
                temp.setCantidad(ctx.formParamAsClass("cantidad",Integer.class).get() + temp.getCantidad());
                carro.changeProducto(temp,posicion);
            }

            ctx.sessionAttribute("carro",carro);
            ctx.redirect("/comprar");
        });

        app.get("/comprar",ctx -> {
           ctx.redirect("/");
        });

        app.get("/ventas",ctx -> {

            if(ctx.cookie("usuario" ) == null || ctx.cookie("password") == null ||
                    !ctx.cookie("usuario").equalsIgnoreCase("admin")){
                ctx.redirect("/auth/ventas");
                    return;
            }
            CarroCompra carro = ctx.sessionAttribute("carro");
            List<VentasProductos> ventas = service.getVentas();
            Map<String,Object> modelo = new HashMap<>();
            modelo.put("ventas",ventas);
            modelo.put("cantidad",carro.getListaProductos().size());

            ctx.render("publico/ventas.vm",modelo);
        });

        app.get("/productos",ctx -> {

            if(ctx.cookie("usuario" ) == null || ctx.cookie("password") == null ||
                    !ctx.cookie("usuario").equalsIgnoreCase("admin")){
                ctx.redirect("/auth/productos");
                 return;
            }
            CarroCompra carro = ctx.sessionAttribute("carro");
            List<Producto> producto = service.getProductos();
            Map<String,Object> modelo = new HashMap<>();
            modelo.put("productos",producto);
            modelo.put("cantidad",carro.getListaProductos().size());

            ctx.render("/publico/productos.vm",modelo);
        });

        app.get("/registrar",ctx -> {
            Map<String,Object>modelo = new HashMap<>();
            modelo.put("accion","/registrar");
            CarroCompra carro = ctx.sessionAttribute("carro");
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
            CarroCompra carro = ctx.sessionAttribute("carro");
            modelo.put("cantidad",carro.getListaProductos().size());
            ctx.render("/publico/registrar.vm",modelo);
        });

        app.post("/edit/{id}",ctx -> {
            String nombre= ctx.formParam("nombre");
            int precio = ctx.formParamAsClass("precio",Integer.class).get();

            Producto tmp = new Producto(nombre,precio);
            tmp.setId(ctx.pathParamAsClass("id",Integer.class).get());
            service.actualizarProducto(tmp);
            ctx.redirect("/productos");

        });

        app.get("/auth/{param}", ctx -> {
            String param = ctx.pathParam("param");
            Map<String, Object> modelo = new HashMap<>();
            modelo.put("param",param);
            ctx.render("/publico/login.vm",modelo);
        });

        app.post("/auth/{param}",ctx -> {
            String user = ctx.formParam("usuario");
            String pass = ctx.formParam("password");
            String tmp = ctx.formParam("param");
            String rec = ctx.formParam("remember");

            if (user == null || pass == null){
                ctx.redirect("/auth/"+tmp);
            }
            Usuario temp= new Usuario(user,pass);
            service.authUser(temp);
            AES256TextEncryptor textEncryptor = new AES256TextEncryptor();
            textEncryptor.setPassword("mipipi");
            pass = textEncryptor.encrypt(pass);
            if (rec !=null) {
                ctx.cookie("usuario", user,(3600*24*7));
                ctx.cookie("password", pass,(3600*24*7));
            }
            ctx.cookie("usuario", user);
            ctx.cookie("password", pass);


            ctx.redirect("/");

        });

        /*Carga el carro pasando la lista de productos que se tiene dentro del carro*/
        app.get("/Carro", ctx -> {
            CarroCompra carro = ctx.sessionAttribute("carro");
            if(carro == null){
                carro = new CarroCompra(service.getCart());
            }
            ctx.sessionAttribute("carro",carro);
            Map<String, Object> modelo = new HashMap<>();
            modelo.put("productos",carro.getListaProductos());
            modelo.put("cantidad",carro.getListaProductos().size());
            ctx.render("/publico/carrito.vm",modelo);
        });
        /*Elimina un producto del carro a partir de su id*/
        app.get("/delete/{id}", ctx -> {
            int id = ctx.pathParamAsClass("id", Integer.class).get();
            CarroCompra carro = ctx.sessionAttribute("carro");
            carro.deleteProductID(id);

            ctx.sessionAttribute("carro",carro);
            ctx.redirect("/Carro");
        });

        /*Procesa la compra*/
        app.post("/procesar",ctx -> {
            CarroCompra carro = ctx.sessionAttribute("carro");
            if(carro.getListaProductos().size() < 1){
                ctx.redirect("/Carro");
            }
            String nombre = ctx.formParam("nombre");
            VentasProductos venta = new VentasProductos(nombre,carro.listaProductos);
            service.addVentas(venta);
            carro.deleteProductos();
            ctx.sessionAttribute("carro",carro);
            ctx.redirect("/comprar");
        });



        app.get("/remove/{id}",ctx -> {
            service.deleteProducto(ctx.pathParamAsClass("id",Integer.class).get());
            ctx.redirect("/productos");

        });

        app.get("/clear",ctx -> {
            CarroCompra carro = ctx.sessionAttribute("carro");
            carro.deleteProductos();
            ctx.redirect("/comprar");
        });

        app.get("/logout",ctx -> {
            if (ctx.cookie("usuario")!=null && ctx.cookie("password")!=null){
                ctx.removeCookie("usuario");
                ctx.removeCookie("password");
            }
            ctx.redirect("/");
        });





    }
}
