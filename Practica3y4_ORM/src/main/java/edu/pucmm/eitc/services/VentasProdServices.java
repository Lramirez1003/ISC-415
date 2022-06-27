package edu.pucmm.eitc.services;

import edu.pucmm.eitc.encapsulaciones.VentasProductos;
import jakarta.persistence.*;
import java.util.List;

public class VentasProdServices  extends DataBaseServices<VentasProductos> {

    static VentasProdServices instancia;

    VentasProdServices() {
        super(VentasProductos.class);
    }

    public static VentasProdServices getInstancia() {
        if (instancia == null) {
            instancia = new VentasProdServices();
        }
        return instancia;
    }


    public List<VentasProductos> getVentasProd() {
        EntityManager em = getEntityManager();
        Query sql= em.createNativeQuery("select * from VentasProductos",VentasProductos.class);
        List<VentasProductos>lista = sql.getResultList();
        return lista;
    }
}
