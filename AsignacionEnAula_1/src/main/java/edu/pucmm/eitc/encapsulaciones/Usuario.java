package edu.pucmm.eitc.encapsulaciones;


import java.util.Set;

/**
 * Clase bajo el formato POJO.
 */
public class Usuario {

    String usuario;
    String nombre;
    String password;
    //lo estaremos utilizando para los roles.


    public Usuario() {
    }

    public Usuario(String usuario, String nombre, String password) {
        this.usuario = usuario;
        this.nombre = nombre;
        this.password = password;
    }



    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
