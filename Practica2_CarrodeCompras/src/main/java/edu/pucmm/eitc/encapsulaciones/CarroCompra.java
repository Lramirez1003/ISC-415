package edu.pucmm.eitc.encapsulaciones;

import java.util.ArrayList;

public class CarroCompra {

    long id;
    ArrayList<Producto> listaProductos;

    public CarroCompra(long id, ArrayList<Producto> productos) {
        this.id = id;
        this.listaProductos = productos;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public ArrayList<Producto> getListaProductos() {
        return listaProductos;
    }

    public void setListaProductos(ArrayList<Producto> listaProductos) {
        this.listaProductos = listaProductos;
    }
}
