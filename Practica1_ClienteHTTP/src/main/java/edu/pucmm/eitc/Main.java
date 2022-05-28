package edu.pucmm.eitc;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.Scanner;

public class Main {

    public static void main(String [] args) throws IOException{
        System.out.println("Ingrese URL deseada: ");

        Scanner scanner= new Scanner(System.in);
        String url= scanner.nextLine();

        Document doc = Jsoup.connect(url).timeout(15000)
                .get();
        //Contador de las lineas del documento
        long lineas= doc.html().lines().count();
        System.out.println("La URL tiene: "+ lineas +" lineas.");
        // Contador de los parrafos(p) del documento
        Elements parrafos=doc.select("p");
        System.out.println("La URL tiene: "+ parrafos.size()+" parrafos.");

        // Contador de imagenes
        if (parrafos.size() != 0) {
            Elements imagen= doc.select("p img");
            System.out.println("La URL tiene: "+ imagen.size() +" imagenes dentro de parrafos.");
        }

        // Formularios post
        Elements posts = doc.select("form[method$=post]");
        System.out.println("La URL tiene: "+ posts.size() +" formularios con el metodo POST.");

        // Formularios get
        Elements gets = doc.select("form[method$=get]");
        System.out.println("La URL tiene: "+ gets.size() +" formularios con el metodo GET.");





    }


}
