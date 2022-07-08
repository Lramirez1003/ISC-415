package edu.pucmm.eitc.services;

import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaQuery;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Clase con patrón Singleton
 * Created by vacax on 27/05/16.
 */
public class DataBaseServices<T> {

    private static EntityManagerFactory emf;
    private Class<T> claseEntidad;
    /**
     *Implementando el patron Singleton
     */
    public  DataBaseServices(Class<T> claseEntidad){
        if (emf == null){
            emf = Persistence.createEntityManagerFactory("MiUnidadPersistencia");
            this.claseEntidad = claseEntidad;
        }
    }
    public EntityManager getEntityManager(){
        return emf.createEntityManager();
    }

    /**
     * Tomado de GestionDB.java creador vacax
     * @return
     */
    public T crear(T entidad) throws IllegalArgumentException, EntityExistsException, PersistenceException{
        EntityManager em = getEntityManager();

        try {

            em.getTransaction().begin();
            em.persist(entidad);
            em.getTransaction().commit();

        }finally {
            em.close();
        }
        return entidad;
    }

    public T editar(T entidad) throws PersistenceException{
        EntityManager em = getEntityManager();
        em.getTransaction().begin();
        try {
            em.merge(entidad);
            em.getTransaction().commit();
        }finally {
            em.close();
        }
        return entidad;
    }

    public boolean eliminar(Object entidadId) throws PersistenceException{
        boolean ok = false;
        EntityManager em = getEntityManager();
        em.getTransaction().begin();
        try {
            T entidad = em.find(claseEntidad, entidadId);
            em.remove(entidad);
            em.getTransaction().commit();
            ok = true;
        }finally {
            em.close();
        }
        return ok;
    }

    public T find(Object id) throws PersistenceException {
        EntityManager em = getEntityManager();
        try{
            return em.find(claseEntidad, id);
        } finally {
            em.close();
        }
    }

    public List<T> findAll() throws PersistenceException {
        EntityManager em = getEntityManager();
        try{
            CriteriaQuery<T> criteriaQuery = em.getCriteriaBuilder().createQuery(claseEntidad);
            criteriaQuery.select(criteriaQuery.from(claseEntidad));
            return em.createQuery(criteriaQuery).getResultList();
        } finally {
            em.close();
        }
    }


    /**
     * Metodo para el registro de driver de conexión.
     */



}
