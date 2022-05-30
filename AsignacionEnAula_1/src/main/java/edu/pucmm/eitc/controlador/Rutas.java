package edu.pucmm.eitc.controlador;

import edu.pucmm.eitc.encapsulaciones.Usuario;
import edu.pucmm.eitc.services.FakeServices;
import edu.pucmm.eitc.util.BaseControlador;
import io.javalin.Javalin;
import io.javalin.http.*;
import io.javalin.plugin.rendering.JavalinRenderer;
import io.javalin.plugin.rendering.template.JavalinFreemarker;
import io.javalin.plugin.rendering.template.JavalinThymeleaf;
import io.javalin.plugin.rendering.template.JavalinVelocity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.javalin.apibuilder.ApiBuilder.*;

public class Rutas extends BaseControlador{

    public Rutas(Javalin app){
        super(app);
    }



    public void aplicarRutas() {


        app.routes(() -> {
            path("/AsigAula1", () -> {

                /**
                 * Validando que exista el usuario por el filtro.
                 */
                before(ctx -> {
                    //recuperando el usuario de la sesión,en caso de no estar, redirecciona la pagina 401.
                    Usuario usuario = ctx.sessionAttribute("usuario");
                    if (usuario == null) {// usuario no existe
                        ctx.redirect("/login.html");
                    }
                    //continuando con la consulta del endpoint solicitado.
                });

                get("/", ctx ->{

                    Usuario usuario = ctx.sessionAttribute("usuario");
                    ctx.redirect("/home.html");
                } );


            });


        });
    }}
