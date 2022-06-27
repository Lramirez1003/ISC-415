package edu.pucmm.eitc.encapsulaciones;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import jakarta.persistence.*;

@Entity
public class VentasProductos implements Serializable {
    @Id
    @GeneratedValue
    long id;
    @Temporal(TemporalType.DATE)
    Date fechaCompra;
    String nombreCliente;
    @OneToMany(fetch = FetchType.EAGER)
    List<Venta> listaProductos;

    public VentasProductos(String nombreCliente) {
        this.nombreCliente = nombreCliente;
        fechaCompra = new Date();
    }
    public VentasProductos(){

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFechaCompra() {
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-YYYY");
        String date = dateFormat.format(fechaCompra);
        return date;
    }

    public void setFechaCompra(Date fechaCompra) {
        this.fechaCompra = fechaCompra;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    public List<Venta> getListaProductos() {
        return listaProductos;
    }

    public void setListaProductos(List<Venta> listaProductos) {
        this.listaProductos = listaProductos;
    }


}

