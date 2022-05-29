package edu.pucmm.eitc;

import org.jsoup.Jsoup;
import org.jsoup.Connection.*;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.Scanner;

public class Main {

    public static void main(String [] args) throws IOException{
        System.out.println("Ingrese URL deseada: ");

        Scanner scanner= new Scanner(System.in);
        String url= scanner.nextLine();

        try {
            Document doc = Jsoup.connect(url).timeout(15000).get();
            //Contador de las lineas del documento
            long lineas= doc.html().lines().count();
            System.out.println("La URL tiene: "+ lineas +" lineas.");
            // Contador de los parrafos(p) del documento
            Elements parrafos=doc.select("p");
            System.out.println("La URL tiene: "+ parrafos.size()+" parrafos.");

            // Contador de imagenes(p img)
            if (parrafos.size() != 0) {
                Elements imagen= doc.select("p img");
                System.out.println("La URL tiene: "+ imagen.size() +" imagenes dentro de parrafos.");
            }

            // Formularios post
            Elements method_post = doc.select("form[method$=post]");
            System.out.println("La URL tiene: "+ method_post.size() +" formularios con el metodo POST.");

            // Formularios get
            Elements method_get = doc.select("form[method$=get]");
            System.out.println("La URL tiene: "+ method_get.size() +" formularios con el metodo GET.");

            campos_input(method_post);
            campos_input(method_get);

            if (method_post.size()!=0) {
                parse_post(method_post);
            }

        } catch (Error e) {
            System.out.println("Ocurrio un error: "+e);
        }

    }

    public static void campos_input(Elements forms) {
        int c = 1;
        for (Element formulario: forms) {
            if (formulario.children().select("input").size() != 0) {
                System.out.println("Los inputs del formulario tipo " + formulario.attr("method").toUpperCase() + " número " + c + " son:");
                for (Element hijo : formulario.select("input")) {
                    System.out.println("\t" + hijo);
                }
            } else {
                System.out.println("El formulario tipo " + formulario.attr("method").toUpperCase() + " número" + c + " no tiene campos input como hijos");
            }
            c++;
        }
    }

    public static void parse_post(Elements forms){
        int c = 1;
        for (Element formulario: forms) {
            try {
                String URL = formulario.attr("action");
                Response respuesta;
                respuesta = Jsoup.connect(URL).method(Method.POST).data("asignatura","practica1").header("matricula","20180570").execute();

                System.out.println("\nRespuesta del formulario "+ formulario.attr("method").toUpperCase()+" número "+c+": \n Status: " + respuesta.statusCode()+ "\n Content-Type: " + respuesta.contentType() +"\n Cookies: "+ respuesta.cookies());
                System.out.println(" Headers: " + respuesta.headers());

            } catch (IOException error) {
                System.out.println("\nError: " + error);
            }
        }
    }
}


