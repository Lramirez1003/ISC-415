package edu.pucmm.eitc;

import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;
import edu.pucmm.eitc.controlador.*;


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

        app.get("/",ctx -> ctx.redirect("/AsigAula1/"));
        app.get("/figAsig1",ctx -> ctx.result("Si"));

        new Rutas(app).aplicarRutas();
        new auth(app).aplicarRutas();

    }

}
