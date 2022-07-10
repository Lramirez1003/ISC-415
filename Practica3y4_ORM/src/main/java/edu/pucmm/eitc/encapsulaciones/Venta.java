package edu.pucmm.eitc.encapsulaciones;

import jakarta.persistence.*;

import java.io.Serializable;

@Entity
public class Venta implements Serializable {
    @Id
    long ProductoId;
    @Id
    long VentaID;
    int Cantidad;
    int Precio;
    String Nombre;

    public Venta(long productoId, long ventaID, int cantidad, int precio, String nombre) {
        this.ProductoId = productoId;
        this.VentaID = ventaID;
        this.Cantidad = cantidad;
        this.Precio = precio;
        this.Nombre = nombre;
    }

    public Venta(){}

    public long getProductoId() {
        return ProductoId;
    }

    public void setProductoId(long productoId) {
        ProductoId = productoId;
    }

    public long getVentaID() {
        return VentaID;
    }

    public void setVentaID(long ventaID) {
        VentaID = ventaID;
    }

    public int getCantidad() {
        return Cantidad;
    }

    public void setCantidad(int cantidad) {
        Cantidad = cantidad;
    }

    public int getPrecio() {
        return Precio;
    }

    public void setPrecio(int precio) {
        Precio = precio;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public int total(){return Cantidad * Precio;}
}
