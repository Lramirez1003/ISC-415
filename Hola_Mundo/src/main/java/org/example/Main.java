package org.example;

import io.javalin.Javalin;


public class Main {
    public static void main(String[] args) {
        String mensaje = "Hola Mundo en Javalin :-D";
        System.out.println(mensaje);
        //Creando la instancia del servidor.
        Javalin app = Javalin.create().start(7000);
        //creando el manejador
        app.get("/", ctx -> ctx.result("Hola Mundo en Javalin :-D"));
    }


}
