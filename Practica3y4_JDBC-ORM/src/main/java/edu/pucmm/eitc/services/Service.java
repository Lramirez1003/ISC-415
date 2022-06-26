package edu.pucmm.eitc.services;

import edu.pucmm.eitc.encapsulaciones.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Service {

    private static Service instance;
    private List<Usuario> listausuarios;
    private List<Producto> listaproducts;
    private List<VentasProductos> listventproduct;
    private int c;
    private long cart;

    public Service() {
        BootStrapServices.startDb();

        DataBaseServices.getInstancia().testConexion();
        try {
            BootStrapServices.Tablas();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public static Service getInstance(){
        if(instance == null){
            instance = new Service();
        }
        return instance;
    }

    public List<Producto> getProductos() {
        List<Producto> listaproducts = new ArrayList<Producto>();
        Connection con = null;
        try {
            String sql ="SELECT * FROM Producto WHERE Estado = 'Disponible'";
            con = DataBaseServices.getInstancia().getConexion();
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs= ps.executeQuery();

            while(rs.next()){
                Producto producto = new Producto();
                producto.setId(rs.getInt("id"));
                producto.setNombre(rs.getString("nombre"));
                producto.setPrecio(rs.getInt("precio"));
                listaproducts.add(producto);
            }
        } catch (SQLException e) {
            Logger.getLogger(Service.class.getName()).log(Level.SEVERE, null, e);

        } finally {
            try {
                con.close();
            } catch (SQLException e) {
                Logger.getLogger(Service.class.getName()).log(Level.SEVERE, null, e);
            }
        }
        return listaproducts;
    }

    public List<VentasProductos> getVentas() {
        List<VentasProductos> listventproduct = new ArrayList<VentasProductos>();
        Connection con = null;
        try {
            String sql ="SELECT * FROM Ventas";
            con = DataBaseServices.getInstancia().getConexion();
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs= ps.executeQuery();

            while(rs.next()){
                VentasProductos ventas = new VentasProductos();
                ventas.setId(rs.getInt("id"));
                ventas.setNombreCliente(rs.getString("nombre"));
                ventas.setFechaCompra(rs.getDate("fecha"));
                ventas.setListaProductos(getProductosVenta(ventas.getId()));
                listventproduct.add(ventas);
            }
        } catch (SQLException e) {
            Logger.getLogger(Service.class.getName()).log(Level.SEVERE, null, e);

        } finally {
            try {
                con.close();
            } catch (SQLException e) {
                Logger.getLogger(Service.class.getName()).log(Level.SEVERE, null, e);
            }
        }
        return listventproduct;
    }

    private ArrayList<Producto> getProductosVenta(long id) {
        List<Producto> productos= new ArrayList<Producto>();
        Connection con = null;
        try{
            String sql = "SELECT Producto.Nombre AS Nombre, Producto.Precio AS Precio,VentaProductos.Cantidad AS Cantidad\n" +
                    "FROM VentaProductos INNER JOIN Producto ON Producto.ID = VentaProductos.ProductoID;";
            con = DataBaseServices.getInstancia().getConexion();
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()){
                Producto producto = new Producto();
                producto.setNombre(rs.getString("nombre"));
                producto.setPrecio(rs.getInt("precio"));
                producto.setCantidad(rs.getInt("cantidad"));
                productos.add(producto);
            }
        }catch (SQLException e){
            Logger.getLogger(Service.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            try {
                con.close();
            }catch (SQLException e){
                Logger.getLogger(Service.class.getName()).log(Level.SEVERE, null, e);
            }
        }
        return (ArrayList<Producto>) productos;
    }

    public Usuario authuser(String usuario, String password){

        return new Usuario(usuario,password);
    }

    public Producto registerProducto(Producto producto){
        int row;
        Connection con= null;
        try {
            String sql = "INSERT INTO Producto(Nombre,Precio,Estado)" +
                    "values(?,?,'Disponible')";
            con = DataBaseServices.getInstancia().getConexion();
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1,producto.getNombre());
            ps.setInt(2,producto.getPrecio());
            row = ps.executeUpdate();

        } catch (SQLException e) {
            Logger.getLogger(Service.class.getName()).log(Level.SEVERE, null, e);
        }finally {
            try {
                con.close();
            }catch (SQLException e){
                Logger.getLogger(Service.class.getName()).log(Level.SEVERE, null, e);
            }
        }


        return producto;
    }

    public long getCart(){
        return cart++;
    }
    public Producto actualizarProducto(Producto producto){
        boolean conf=false;
        int row;
        Connection con= null;
        try {
            String sql = "UPDATE Producto SET Nombre=?, Precio=? where ID= ?";
            con = DataBaseServices.getInstancia().getConexion();
            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1,producto.getNombre());
            ps.setInt(2,producto.getPrecio());
            ps.setInt(3,producto.getId());
            row = ps.executeUpdate();
            conf =row>0;

        } catch (SQLException e) {
            Logger.getLogger(Service.class.getName()).log(Level.SEVERE, null, e);
        }finally {
            try {
                con.close();
            }catch (SQLException e){
                Logger.getLogger(Service.class.getName()).log(Level.SEVERE, null, e);
            }
        }

        return producto;
    }

    public boolean deleteProducto(int id){
        boolean conf=false;
        int row;
        Connection con= null;
        try {
            String sql = "UPDATE Producto SET Estado = 'Eliminado' where ID= ?";
            con = DataBaseServices.getInstancia().getConexion();
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1,id);
            row = ps.executeUpdate();
            conf =row>0;

        } catch (SQLException e) {
            Logger.getLogger(Service.class.getName()).log(Level.SEVERE, null, e);
        }finally {
            try {
                con.close();
            }catch (SQLException e){
                Logger.getLogger(Service.class.getName()).log(Level.SEVERE, null, e);
            }
        }

        return conf;
    }

    public Producto getProductoID(int id) {
        Producto temp=null;
        Connection con= null;
        try {
            String sql = "SELECT * FROM Producto where ID= ?";
            con = DataBaseServices.getInstancia().getConexion();
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1,id);
            ResultSet rs = ps.executeQuery();

            while (rs.next()){
                temp = new Producto();
                temp.setId(id);
                temp.setNombre(rs.getString("Nombre"));
                temp.setPrecio(rs.getInt("Precio"));

            }

        } catch (SQLException e) {
            Logger.getLogger(Service.class.getName()).log(Level.SEVERE, null, e);
        }finally {
            try {
                con.close();
            }catch (SQLException e){
                Logger.getLogger(Service.class.getName()).log(Level.SEVERE, null, e);
            }
        }

        return temp;
    }

    public boolean addVentas(VentasProductos venta) {
        boolean conf=false;
        int row;
        Connection con= null;
        try {
            String sql = "INSERT INTO Ventas(Fecha,Nombre)" +
                    "values(CURRENT_DATE,?)";
            con = DataBaseServices.getInstancia().getConexion();
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1,venta.getNombreCliente());
            row = ps.executeUpdate();
            addVentasProducto(row,venta.getListaProductos());
            conf =row>0;

        } catch (SQLException e) {
            Logger.getLogger(Service.class.getName()).log(Level.SEVERE, null, e);
        }finally {
            try {
                con.close();
            }catch (SQLException e){
                Logger.getLogger(Service.class.getName()).log(Level.SEVERE, null, e);
            }
        }

        return conf;
    }

    private boolean addVentasProducto(int row, ArrayList<Producto> listaProductos) {
        boolean conf=false;
        Connection con= null;
        for (Producto producto:listaProductos){
            try {
                String sql="INSERT INTO VentaProductos(VentaID,ProductoID,Cantidad)" +
                        "values(?,?,?)";
                con = DataBaseServices.getInstancia().getConexion();
                PreparedStatement ps = con.prepareStatement(sql);
                ps.setInt(1,row);
                ps.setInt(2,producto.getId());
                ps.setInt(3,producto.getCantidad());

                int exec = ps.executeUpdate();
                conf = exec > 0;

            } catch (SQLException e) {
                Logger.getLogger(Service.class.getName()).log(Level.SEVERE, null, e);
            }finally {
                try {
                    con.close();
                }catch (SQLException e){
                    Logger.getLogger(Service.class.getName()).log(Level.SEVERE, null, e);
                }
            }

        }
        return conf;
    }
    private void registerUsuario(Usuario user) throws SQLException {
        String sql ="INSERT INTO Usuario(USUARIO,PASSWORD,TIPO) " +
                "SELECT * FROM (select '"+user.getUsuario()+"' as us,'"+user.getPassword()+"' as pas, 'User' as ti) as val1 " +
                "WHERE NOT EXISTS (select * from usuario where usuario='"+user.getUsuario()+"' limit 1)";
        BootStrapServices.ExecuteQuery(sql);
    }

    public String authUser(Usuario user){
        boolean conf= false;
        int res;
        String tipo;
        Connection con = null;
        try {
            String sql = "SELECT COUNT(*) AS Cantidad FROM Usuario WHERE User = '"+user.getUsuario()+"'and Password = '" +user.getPassword()+"'";
            con = DataBaseServices.getInstancia().getConexion();
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            rs.next();
            res = rs.getInt("cantidad");
            if (res > 0) {
                String sql1 = "SELECT Tipo FROM Usuario WHERE User = '"+user.getUsuario()+"'and Password = '"+user.getPassword()+"'";
                ps = con.prepareStatement(sql1);
                ResultSet rs1 = ps.executeQuery();
                rs1.next();
                tipo = rs1.getString("tipo");
                return tipo;
            }else{
                    registerUsuario(user);
                }
        }  catch (SQLException e) {
            Logger.getLogger(Service.class.getName()).log(Level.SEVERE, null, e);
        }finally {
            try {
                con.close();
            }catch (SQLException e){
                Logger.getLogger(Service.class.getName()).log(Level.SEVERE, null, e);
            }
        }

        return "";
    }



}
