package edu.pucmm.eitc.services;

import edu.pucmm.eitc.encapsulaciones.Producto;
import edu.pucmm.eitc.encapsulaciones.Venta;
import edu.pucmm.eitc.services.DataBaseServices;


import java.util.ArrayList;
import java.util.List;

public class VentaServices extends DataBaseServices<Venta> {

    private static VentaServices instancia;

    VentaServices() {
        super(Venta.class);
    }

    public static VentaServices getInstancia() {
        if (instancia == null) {
            instancia = new VentaServices();
        }
        return instancia;
    }

    public List<Venta> VentaRealizada(List<Producto> products, long venta) {
        List<Venta> lista = new ArrayList<Venta>();
        for (Producto prod:products){
            Venta tmp = new Venta(prod.getId(),venta,prod.getCantidad(),prod.getPrecio(),prod.getNombre());
            getInstancia().crear(tmp);
            lista.add(tmp);
        }

        return lista;
    }
}

