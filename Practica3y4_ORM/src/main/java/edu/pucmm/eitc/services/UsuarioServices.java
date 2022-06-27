package edu.pucmm.eitc.services;

import edu.pucmm.eitc.encapsulaciones.Usuario;

public class UsuarioServices extends DataBaseServices<Usuario>{

    static UsuarioServices instancia;

    UsuarioServices() {
        super(Usuario.class);
    }

    public static UsuarioServices getInstancia() {
        if (instancia == null) {
            instancia = new UsuarioServices();
        }
        return instancia;
    }

    public static String authUser(Usuario user){
        return "admin";
    }
}
