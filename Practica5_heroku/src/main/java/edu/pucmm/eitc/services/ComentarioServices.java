package edu.pucmm.eitc.services;

import org.hibernate.*;
import edu.pucmm.eitc.encapsulaciones.Comentario;
import edu.pucmm.eitc.services.DataBaseServices;

import jakarta.persistence.*;

import java.util.List;

public class ComentarioServices extends DataBaseServices<Comentario> {

    private static ComentarioServices instancia;

    ComentarioServices(){
        super(Comentario.class);
    }

    public static ComentarioServices getInstancia(){
        if(instancia==null){
            instancia = new ComentarioServices();
        }
        return instancia;
    }


    public List<Comentario> findComentarios(int id)  {
        EntityManager em = getEntityManager();
        Query sql = em.createQuery("select e from Comentario e where e.Estado = true and e.ProductoId like :id");
        sql.setParameter("id",id);
        List<Comentario> lista = sql.getResultList();
        return lista;
    }

    public void deleteComentario(int id) throws PersistenceException{
        Session sesion = getEntityManager().unwrap(Session.class);
        sesion.beginTransaction();
        Query sql = sesion.createQuery("delete from Comentario where id="+id);
        int row = sql.executeUpdate();
        sesion.getTransaction().commit();
        sesion.close();
    }


}
