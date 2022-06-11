package edu.pucmm.eitc.services;

import edu.pucmm.eitc.encapsulaciones.*;

import java.util.ArrayList;
import java.util.List;

public class Service {

    static Service instance;
    List<Usuario> listausuarios;
    List<Producto> listaproducts;
    List<VentasProductos> listventproduct;
    int c;
    long cart;

    public Service() {
        listausuarios = new ArrayList<>();
        listaproducts = new ArrayList<>();
        listventproduct = new ArrayList<>();
        c= 0;
        cart = 0;
        listausuarios.add(new Usuario("admin","admin","admin"));
    }

    public static Service getInstance(){
        if(instance == null){
            instance = new Service();
        }
        return instance;
    }

    public List<Usuario> getUsuarios() {
        return listausuarios;
    }

    public List<Producto> getProductos() {
        return listaproducts;
    }

    public List<VentasProductos> getVentas() {
        return listventproduct;
    }

    public Usuario authuser(String usuario,String nombre, String password){

        return new Usuario(usuario,nombre,password);
    }

    public Producto registerProducto(Producto producto){
        producto.setId(c++);
        listaproducts.add(producto);
        return producto;
    }

    public long getCart(){
        return cart++;
    }
    public Producto actualizarProducto(Producto producto){
        Producto tmp = getProductoID(producto.getId());
        if(tmp == null){
            throw new RuntimeException("El usuario no existe: "+producto.getId());
        }
        tmp.act_prod(producto);
        return tmp;
    }

    public boolean deleteProducto(int id){
        Producto temp = getProductoID(id);
        return listaproducts.remove(temp);
    }
    public Producto getProductoID(int id) {
        return listaproducts.stream().filter(e -> e.getId() == id).findFirst().orElse(null);
    }

    public void addVentas(VentasProductos venta) {
        listventproduct.add(venta);
    }

}
