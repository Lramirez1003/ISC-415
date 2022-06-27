package edu.pucmm.eitc.services;

import edu.pucmm.eitc.services.DataBaseServices;
import edu.pucmm.eitc.encapsulaciones.Producto;
import jakarta.persistence.*;

import java.util.List;

public class ProductoServices extends DataBaseServices<Producto> {

    static ProductoServices instancia;

    ProductoServices() {
        super(Producto.class);
    }

    public static ProductoServices getInstancia() {
        if (instancia == null) {
            instancia = new ProductoServices();
        }
        return instancia;
    }

    public void BorrarProd(Object id){
        Producto ent=find(id);
        ent.setEstado(false);
        ent = editar(ent);
    }

    public List<Producto> EncontrarProd(int in, int out) throws PersistenceException{
        EntityManager em = getEntityManager();
        Query sql = em.createNativeQuery("select * from Producto where Estado = true",Producto.class);
        sql.setFirstResult(in);
        if(out !=0){
            sql.setMaxResults(out);
        }
        List<Producto> lista = sql.getResultList();
        return lista;
    }

    public int pagina(){
        int size = 10;
        EntityManager em = getEntityManager();
        Query sql = em.createNativeQuery("select * from Producto where Estado = true",Producto.class);
        int rs = sql.getResultList().size();
        int ultima = (int)(Math.ceil(rs/size));
        return ultima;
    }

}
