package edu.pucmm.eitc.encapsulaciones;

import java.util.ArrayList;

public class CarroCompra {

    long id;
    ArrayList<Producto> listaProductos;

    public CarroCompra(long id) {
        this.id = id;
        this.listaProductos = new ArrayList<Producto>();
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
    public Producto getProductID(int id){
        return listaProductos.stream().filter(e->e.getId()==id).findFirst().orElse(null);
    }
    public void addProducto(Producto producto) {
        this.listaProductos.add(producto);
    }

    public void changeProducto(Producto temp, int posicion){
        listaProductos.set(posicion,temp);
    }

    public int getPosicion(int id){
        int c = 0;
        while (c < listaProductos.size()){
            if(listaProductos.get(c).getId() == id){
                return c;
            }
            c++;
        }
        return -1;
    }

    public void deleteProductID(int id){
        int c = getPosicion(id);
        listaProductos.remove(c);

    }

    public void deleteProductos(){
        this.listaProductos= new ArrayList<Producto>();
    }

}
