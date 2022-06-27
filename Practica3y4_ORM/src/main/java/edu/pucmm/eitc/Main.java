package edu.pucmm.eitc;

import edu.pucmm.eitc.services.BootStrapServices;
import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;
import edu.pucmm.eitc.enrutamiento.*;
import io.javalin.plugin.rendering.JavalinRenderer;
import io.javalin.plugin.rendering.template.JavalinVelocity;


public class Main {
    private static void registrandoPlantillas(){
        JavalinRenderer.register(JavalinVelocity.INSTANCE, ".vm");
    }

    public static void main(String[] args){


        BootStrapServices.startDb();
        Javalin app = Javalin.create(config ->{
            config.addStaticFiles(staticFileConfig -> {
                staticFileConfig.hostedPath = "/";
                staticFileConfig.directory = "/publico";
                staticFileConfig.location = Location.CLASSPATH;
            });

        });
        app.start(7000);
        registrandoPlantillas();
        new rutas(app).aplicarRutas();




    }
}