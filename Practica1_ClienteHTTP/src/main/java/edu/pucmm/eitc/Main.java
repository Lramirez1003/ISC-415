package edu.pucmm.eitc;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.Scanner;

public class Main {

    public static void main(String [] args) throws IOException{
        System.out.println("Ingrese URL deseada: ");

        Scanner scanner= new Scanner(System.in);
        String url= scanner.nextLine();

        try {


            Document doc = Jsoup.connect(url).timeout(15000)
                    .followRedirects(true)
                    .get();
            //Contador de las lineas del documento
            long lineas= doc.html().lines().count();
            System.out.println("La URL tiene: "+ lineas +" lineas.");
            // Contador de los parrafos(p) del documento
            long parrafos=doc.html().  ;

            System.out.println("Su URL es: "+ url +" .");

        } catch (Exception e) {
            throw new RuntimeException(e);
        }


    }


}
