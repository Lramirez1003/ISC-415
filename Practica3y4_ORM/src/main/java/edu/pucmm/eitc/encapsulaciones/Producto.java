package edu.pucmm.eitc.encapsulaciones;

public class Producto {

    int id;
    String nombre;
    int precio;

    int Cantidad;

    public Producto(String nombre, int precio) {
        this.nombre = nombre;
        this.precio = precio;
    }

    public Producto() {
        id= 0;
        nombre="";
        precio=0;
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

    public int total(){
        return precio * Cantidad;
    }


    public void act_prod(Producto producto){
        this.nombre = producto.nombre;
        this.precio = producto.precio;

    }
}
