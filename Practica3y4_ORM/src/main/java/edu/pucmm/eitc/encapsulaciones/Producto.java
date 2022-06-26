package edu.pucmm.eitc.encapsulaciones;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.List;

@Entity
public class Producto implements Serializable {

    @Id
    @GeneratedValue
    int id;
    String nombre;
    int precio;

    @Transient
    int Cantidad;
    String Descripcion;

    @Column(columnDefinition = "boolean default true")
    boolean Estado;

    @OneToMany(fetch = FetchType.EAGER)
    List<Foto> Fotos;

    public Producto(String nombre, int precio, String Descripcion) {
        this.nombre = nombre;
        this.precio = precio;
        this.Descripcion = Descripcion;
        Estado = true;
    }

    public Producto() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getPrecio() {
        return precio;
    }

    public void setPrecio(int precio) {
        this.precio = precio;
    }

    public int getCantidad() {
        return Cantidad;
    }

    public void setCantidad(int cantidad) {
        Cantidad = cantidad;
    }

    public String getDescripcion() {
        return Descripcion;
    }

    public void setDescripcion(String descripcion) {
        Descripcion = descripcion;
    }

    public boolean isEstado() {
        return Estado;
    }

    public void setEstado(boolean estado) {
        Estado = estado;
    }

    public List<Foto> getFotos() {
        return Fotos;
    }

    public void setFotos(List<Foto> fotos) {
        Fotos = fotos;
    }
}
