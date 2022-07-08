package edu.pucmm.eitc.enrutamiento;

import edu.pucmm.eitc.services.*;
import edu.pucmm.eitc.util.BaseControlador;
import io.javalin.Javalin;

import edu.pucmm.eitc.encapsulaciones.*;
import org.jasypt.util.text.AES256TextEncryptor;

import java.io.IOException;
import java.util.*;

public class rutas extends BaseControlador {


    public rutas(Javalin app) {
        super(app);
    }
    @Override
    public void aplicarRutas() {

        Paginacion();

        app.before(ctx -> {
            CarroCompra cart = ctx.sessionAttribute("carro");
            if(cart == null){
                cart = new CarroCompra();
            }
            ctx.sessionAttribute("carro",cart);

        });

        app.get("/", ctx -> {
            CarroCompra carro = ctx.sessionAttribute("carro");

            List<Producto> productos = ProductoServices.getInstancia().EncontrarProd(0,10) ;
            Map<String, Object> modelo = new HashMap<>();
            modelo.put("productos",productos);
            modelo.put("cantidad",carro.getListaProductos().size());
            List<String> page = getPages();
            modelo.put("paginas",page);
            ctx.render("/publico/listaProd.vm", modelo);
        });

        app.post("/comprar", ctx -> {
            CarroCompra carro = ctx.sessionAttribute("carro");

            Producto temp = carro.getProductID(ctx.formParamAsClass("id",
                    Integer.class).get());
            if(temp == null){
                temp = ProductoServices.getInstancia().find(ctx.formParamAsClass("id", Integer.class).get());
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
            List<VentasProductos> ventas = VentasProdServices.getInstancia().getVentasProd();
            Map<String,Object> modelo = new HashMap<>();
            modelo.put("ventas",ventas);
            modelo.put("cantidad",carro.getListaProductos().size());

            ctx.render("/publico/ventas.vm",modelo);
        });

        app.get("/productos",ctx -> {

            if(ctx.cookie("usuario" ) == null || ctx.cookie("password") == null ||
                    !ctx.cookie("usuario").equalsIgnoreCase("admin")){
                ctx.redirect("/auth/productos");
                 return;
            }
            CarroCompra carro = ctx.sessionAttribute("carro");
            List<Producto> producto = ProductoServices.getInstancia().EncontrarProd(0,0);
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
            ctx.render("/publico/registrar.vm",modelo);
        });

        app.post("/registrar",ctx -> {
            String nombre= ctx.formParam("nombre");
            int precio = ctx.formParamAsClass("precio",Integer.class).get();
            String descripcion = ctx.formParam("desc");
            List<Foto> images = new ArrayList<Foto>();
            ctx.uploadedFiles("img").forEach(uploadedFile -> {
                try {
                    byte[]bytes = uploadedFile.getContent().readAllBytes();
                    String encString = Base64.getEncoder().encodeToString(bytes);
                    Foto foto = new Foto(uploadedFile.getFilename(), uploadedFile.getContentType(), encString);
                    FotoServices.getInstancia().crear(foto);
                    images.add(foto);
                }catch (IOException ex){
                    ex.printStackTrace();
                }
            });
            Producto tmp = new Producto(nombre,precio,descripcion);
            tmp.setFotos(images);
            ProductoServices.getInstancia().crear(tmp);
            ctx.redirect("/productos");
        });
        app.get("/visualizar/{id}",ctx -> {
            int id = ctx.pathParamAsClass("id",Integer.class).get();
            Producto tmp = ProductoServices.getInstancia().find(id);
            List<Comentario> comment = ComentarioServices.getInstancia().findComentarios(tmp.getId());
            Map<String,Object> modelo = new HashMap<>();
            String usuario = ctx.cookie("usuario");
            modelo.put("temp",tmp);
            modelo.put("comentarios", comment);
            modelo.put("user",usuario);
            ctx.render("/publico/vis.vm",modelo);
        });
        app.post("/addComentario/{id}", ctx->{
            String comentario = ctx.formParam("comentarios");
            int id = ctx.pathParamAsClass("id", Integer.class).get();
            Comentario tmp = new Comentario(comentario,id);
            ComentarioServices.getInstancia().crear(tmp);
            ctx.redirect("/visualizar/"+id);
        });

        app.get("/delComentario/{id}/{comentarios}", ctx ->{
            int id = ctx.pathParamAsClass("id", Integer.class).get();
            int com = ctx.pathParamAsClass("comentarios",Integer.class).get();
            ComentarioServices.getInstancia().deleteComentario(com);
           ctx.redirect("/visualizar/"+id);
        });

        app.get("/edit/{id}",ctx -> {
            Producto tmp = ProductoServices.getInstancia().find(ctx.pathParamAsClass("id",Integer.class).get());
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
            String desc= ctx.formParam("desc") ;
            Producto tmp = new Producto(nombre,precio,desc);
            tmp.setId(ctx.pathParamAsClass("id",Integer.class).get());
            ProductoServices.getInstancia().editar(tmp);
            ctx.redirect("/productos");

        });

        app.get("/comprar/{id}", ctx -> {
            int pos = ctx.pathParamAsClass("id", Integer.class).get() * 10;
            CarroCompra carro = ctx.sessionAttribute("carro");
            List<Producto> productos = ProductoServices.getInstancia().EncontrarProd(pos,pos+10);
            Map<String, Object> modelo = new HashMap<>();
            modelo.put("productos",productos);
            modelo.put("cantidad",carro.getListaProductos().size());
            List<String> paginas = getPages();
            modelo.put("paginas",paginas);
            ctx.render("/publico/listaProd.vm", modelo);
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


        app.get("/Carro", ctx -> {
            CarroCompra carro = ctx.sessionAttribute("carro");
            if(carro == null){
                carro = new CarroCompra();
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
            VentasProductos ventap = new VentasProductos(nombre);
            List<Venta> lista = VentaServices.getInstancia().VentaRealizada(carro.listaProductos,ventap.getId());
            ventap.setListaProductos(lista);
            VentaServices.getInstancia().crearVenta(ventap);
            carro.deleteProductos();
            ctx.sessionAttribute("carro",carro);
            ctx.redirect("/comprar");
        });



        app.get("/remove/{id}",ctx -> {
            ProductoServices.getInstancia().eliminar(ctx.pathParamAsClass("id",Integer.class).get());
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

    public void Paginacion() {
        String nombre;
        int precio;
        String descripcion;
        List<Foto> Fotos = new ArrayList<Foto>();
        for(int i = 0 ; i < 19; i++){
            nombre = "Producto #"+ i;
            precio = 10 * i;
            descripcion = "Este es el producto "+i;
            Producto pag = new Producto(nombre,precio,descripcion);
            pag.setFotos(Fotos);
            ProductoServices.getInstancia().crear(pag);
        }


    }

    static List<String> getPages(){
        int page = ProductoServices.getInstancia().pagina();
        List<String> lista = new ArrayList<String>();
        for(int i = 0; i <= page; i++){
            String aux = "<a class=\"page-link\" href=\"/comprar/"+i+"\">"+(i+1)+"</a>";
            lista.add(aux);
        }
        return lista;
    }

}
