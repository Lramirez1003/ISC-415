package edu.pucmm.eitc.services;

import org.h2.tools.Server;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by vacax on 27/05/16.
 */
public class BootStrapServices {

    private static Server server;

    /**
     *
     * @throws SQLException
     */
    public static void startDb()  {
        try {
            server = Server.createTcpServer("-tcpPort", "9092", "-tcpAllowOthers", "-ifNotExists").start();
        }catch (SQLException ex){
            ex.printStackTrace();
        }
    }

    /**
     *
     * @throws SQLException
     */
    public static void stopDb() throws SQLException {
        if(server!=null) {
            server.stop();
        }
    }


    /**
     * Metodo para recrear las tablas necesarios
     * @throws SQLException
     */
    public static void  Tablas() throws SQLException{
        TablaProducto();
        TablaUsuario();
        TablaVentas();
        TablaVentasProductos();
        Admin();
    }
    public static void  ExecuteQuery(String query) throws SQLException{
        Connection con = DataBaseServices.getInstancia().getConexion();
        Statement statement = con.createStatement();
        statement.execute(query);
        statement.close();
        con.close();
    }


    public static void TablaProducto() throws  SQLException{
        String sql = "CREATE TABLE IF NOT EXISTS Producto\n" +
                "(\n" +
                "  ID IDENTITY PRIMARY KEY NOT NULL,\n" +
                "  Nombre VARCHAR(100) NOT NULL,\n" +
                "  EstadoVARCHAR(5) NOT NULL,\n" +
                "  Precio VARCHAR(25) NOT NULL,\n" +
                  ");";
        ExecuteQuery(sql);

    }

    public static void TablaUsuario() throws  SQLException{
        String sql = "CREATE TABLE IF NOT EXISTS Usuario\n" +
                "(\n" +
                "  User IDENTITY PRIMARY KEY NOT NULL,\n" +
                "  Password VARCHAR(100) NOT NULL,\n" +
                "  Tipo VARCHAR(5) NOT NULL,\n" +
                ");";
        ExecuteQuery(sql);

    }

    public static void  Admin() throws SQLException{
        String sql = "INSERT INTO Usuario (User,Password,Tipo) " +
                "values ('admin','admin','admin') ";
        Connection con = null;
        try {
            con = DataBaseServices.getInstancia().getConexion();
            PreparedStatement ps= con.prepareStatement(sql);
            int row = ps.executeUpdate();

        } catch (SQLException e) {
            Logger.getLogger(Service.class.getName()).log(Level.SEVERE, null, e);

            throw new RuntimeException(e);
        } finally {
            try {
                con.close();
            } catch (SQLException e) {
                Logger.getLogger(Service.class.getName()).log(Level.SEVERE, null, e);

                throw new RuntimeException(e);
            }
        }

    }

    public static void TablaVentas() throws  SQLException{
        String sql = "CREATE TABLE IF NOT EXISTS Ventas\n" +
                "(\n" +
                "  ID IDENTITY PRIMARY KEY NOT NULL,\n" +
                "  Fecha DATE NOT NULL,\n" +
                "  Nombre VARCHAR(25) NOT NULL,\n" +
                ");";
        ExecuteQuery(sql);

    }

    public static void TablaVentasProductos() throws  SQLException{
        String sql = "CREATE TABLE IF NOT EXISTS Ventas\n" +
                "(\n" +
                "  ID IDENTITY PRIMARY KEY NOT NULL,\n" +
                "  Fecha DATE NOT NULL,\n" +
                "  Nombre VARCHAR(25) NOT NULL,\n" +
                ");";
        ExecuteQuery(sql);

    }





}

