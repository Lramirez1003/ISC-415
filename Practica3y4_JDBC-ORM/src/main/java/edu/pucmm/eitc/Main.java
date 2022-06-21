package edu.pucmm.eitc;

import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;
import edu.pucmm.eitc.enrutamiento.*;
import io.javalin.plugin.rendering.JavalinRenderer;
import io.javalin.plugin.rendering.template.JavalinThymeleaf;
import io.javalin.plugin.rendering.template.JavalinVelocity;

public class Main {

    public static void main(String[] args){

        Javalin app = Javalin.create(config ->{
            config.addStaticFiles(staticFileConfig -> {
                staticFileConfig.hostedPath = "/";
                staticFileConfig.directory = "/publico";
                staticFileConfig.location = Location.CLASSPATH;
            });

        });
        app.start(7000);
        new rutas(app).aplicarRutas();




    }
}