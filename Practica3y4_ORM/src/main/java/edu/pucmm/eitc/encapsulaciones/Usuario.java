package edu.pucmm.eitc.encapsulaciones;

import java.io.Serializable;
import java.security.PublicKey;
import jakarta.persistence.*;

@Entity
public class Usuario implements Serializable {
    @Id
    String usuario;
    String password;

    public Usuario(String usuario,String password){
        this.usuario = usuario;
        this.password = password;
    }

    public Usuario() {
        usuario ="";
        password="";
    }


    public String getUsuario() {
        return usuario;
    }


    public String getPassword() {
        return password;
    }


}
