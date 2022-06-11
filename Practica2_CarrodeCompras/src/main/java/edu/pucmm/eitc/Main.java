package edu.pucmm.eitc;

import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;
import edu.pucmm.eitc.enrutamiento.*;

public class Main {

    public static void main(String[] args){

        Javalin app = Javalin.create(config ->{
            config.addStaticFiles(staticFileConfig -> {
                staticFileConfig.hostedPath = "/";
                staticFileConfig.directory = "/publico";
                staticFileConfig.location = Location.CLASSPATH;
            });

        });
        app.start();

        //new rutas(app).aplicarRutas();




    }
}