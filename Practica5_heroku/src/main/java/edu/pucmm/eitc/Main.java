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

    private static String modoConexion="";

    public static void main(String[] args){

        if(args.length >= 1){
            modoConexion = args[0];
            System.out.println("Modo de Operacion: "+modoConexion);
        }

        //Iniciando la base de datos.
        /*if(modoConexion.isEmpty()) {
            BootStrapServices.getInstancia().init();
        }*/


        //BootStrapServices.startDb();
        Javalin app = Javalin.create(config ->{
            config.addStaticFiles(staticFileConfig -> {
                staticFileConfig.hostedPath = "/";
                staticFileConfig.directory = "/publico";
                staticFileConfig.location = Location.CLASSPATH;
            });

        });
        app.start(getHerokuAssignedPort());
        registrandoPlantillas();
        new rutas(app).aplicarRutas();




    }


    public static int getHerokuAssignedPort(){
        ProcessBuilder processBuilder = new ProcessBuilder();
        if (processBuilder.environment().get("PORT") != null) {
            return Integer.parseInt(processBuilder.environment().get("PORT"));
        }
        return 7000; //Retorna el puerto por defecto en caso de no estar en Heroku.
    }

    public static String getModoConexion() {
        return modoConexion;
    }
}