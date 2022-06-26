package edu.pucmm.eitc.services;

import edu.pucmm.eitc.services.DataBaseServices;
import edu.pucmm.eitc.encapsulaciones.Foto;



import java.util.List;

/**
 *
 */
public class FotoServices extends DataBaseServices<Foto> {

    private static FotoServices instancia;

    private FotoServices(){
        super(Foto.class);
    }

    public static FotoServices getInstancia(){
        if(instancia==null){
            instancia = new FotoServices();
        }
        return instancia;
    }

}
