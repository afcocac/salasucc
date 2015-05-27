/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.andco.salasucc.controller;

import com.andco.salasucc.controller.exceptions.NonexistentEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.andco.salasucc.model.MaterialesAdicionales;
import com.andco.salasucc.model.Reservas;
import com.andco.salasucc.model.Salas;
import com.andco.salasucc.model.Usuarios;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author ANDCO
 */
public class ReservasJpaController implements Serializable {

    public ReservasJpaController() {
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("com.andco_salasucc_war_1.0-SNAPSHOTPU");
        this.emf = factory;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Reservas reservas) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            MaterialesAdicionales idmaterialAdicional = reservas.getIdmaterialAdicional();
            if (idmaterialAdicional != null) {
                idmaterialAdicional = em.getReference(idmaterialAdicional.getClass(), idmaterialAdicional.getIdmaterialAdicional());
                reservas.setIdmaterialAdicional(idmaterialAdicional);
            }
            Salas idsala = reservas.getIdsala();
            if (idsala != null) {
                idsala = em.getReference(idsala.getClass(), idsala.getIdsala());
                reservas.setIdsala(idsala);
            }
            Usuarios idusuario = reservas.getIdusuario();
            if (idusuario != null) {
                idusuario = em.getReference(idusuario.getClass(), idusuario.getIdusuario());
                reservas.setIdusuario(idusuario);
            }
            em.persist(reservas);
            if (idmaterialAdicional != null) {
                idmaterialAdicional.getReservasList().add(reservas);
                idmaterialAdicional = em.merge(idmaterialAdicional);
            }
            if (idsala != null) {
                idsala.getReservasList().add(reservas);
                idsala = em.merge(idsala);
            }
            if (idusuario != null) {
                idusuario.getReservasList().add(reservas);
                idusuario = em.merge(idusuario);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Reservas reservas) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Reservas persistentReservas = em.find(Reservas.class, reservas.getIdreserva());
            MaterialesAdicionales idmaterialAdicionalOld = persistentReservas.getIdmaterialAdicional();
            MaterialesAdicionales idmaterialAdicionalNew = reservas.getIdmaterialAdicional();
            Salas idsalaOld = persistentReservas.getIdsala();
            Salas idsalaNew = reservas.getIdsala();
            Usuarios idusuarioOld = persistentReservas.getIdusuario();
            Usuarios idusuarioNew = reservas.getIdusuario();
            if (idmaterialAdicionalNew != null) {
                idmaterialAdicionalNew = em.getReference(idmaterialAdicionalNew.getClass(), idmaterialAdicionalNew.getIdmaterialAdicional());
                reservas.setIdmaterialAdicional(idmaterialAdicionalNew);
            }
            if (idsalaNew != null) {
                idsalaNew = em.getReference(idsalaNew.getClass(), idsalaNew.getIdsala());
                reservas.setIdsala(idsalaNew);
            }
            if (idusuarioNew != null) {
                idusuarioNew = em.getReference(idusuarioNew.getClass(), idusuarioNew.getIdusuario());
                reservas.setIdusuario(idusuarioNew);
            }
            reservas = em.merge(reservas);
            if (idmaterialAdicionalOld != null && !idmaterialAdicionalOld.equals(idmaterialAdicionalNew)) {
                idmaterialAdicionalOld.getReservasList().remove(reservas);
                idmaterialAdicionalOld = em.merge(idmaterialAdicionalOld);
            }
            if (idmaterialAdicionalNew != null && !idmaterialAdicionalNew.equals(idmaterialAdicionalOld)) {
                idmaterialAdicionalNew.getReservasList().add(reservas);
                idmaterialAdicionalNew = em.merge(idmaterialAdicionalNew);
            }
            if (idsalaOld != null && !idsalaOld.equals(idsalaNew)) {
                idsalaOld.getReservasList().remove(reservas);
                idsalaOld = em.merge(idsalaOld);
            }
            if (idsalaNew != null && !idsalaNew.equals(idsalaOld)) {
                idsalaNew.getReservasList().add(reservas);
                idsalaNew = em.merge(idsalaNew);
            }
            if (idusuarioOld != null && !idusuarioOld.equals(idusuarioNew)) {
                idusuarioOld.getReservasList().remove(reservas);
                idusuarioOld = em.merge(idusuarioOld);
            }
            if (idusuarioNew != null && !idusuarioNew.equals(idusuarioOld)) {
                idusuarioNew.getReservasList().add(reservas);
                idusuarioNew = em.merge(idusuarioNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = reservas.getIdreserva();
                if (findReservas(id) == null) {
                    throw new NonexistentEntityException("The reservas with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Reservas reservas;
            try {
                reservas = em.getReference(Reservas.class, id);
                reservas.getIdreserva();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The reservas with id " + id + " no longer exists.", enfe);
            }
            MaterialesAdicionales idmaterialAdicional = reservas.getIdmaterialAdicional();
            if (idmaterialAdicional != null) {
                idmaterialAdicional.getReservasList().remove(reservas);
                idmaterialAdicional = em.merge(idmaterialAdicional);
            }
            Salas idsala = reservas.getIdsala();
            if (idsala != null) {
                idsala.getReservasList().remove(reservas);
                idsala = em.merge(idsala);
            }
            Usuarios idusuario = reservas.getIdusuario();
            if (idusuario != null) {
                idusuario.getReservasList().remove(reservas);
                idusuario = em.merge(idusuario);
            }
            em.remove(reservas);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Reservas> findReservasEntities() {
        return findReservasEntities(true, -1, -1);
    }

    public List<Reservas> findReservasEntities(int maxResults, int firstResult) {
        return findReservasEntities(false, maxResults, firstResult);
    }

    private List<Reservas> findReservasEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Reservas.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Reservas findReservas(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Reservas.class, id);
        } finally {
            em.close();
        }
    }

    public int getReservasCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Reservas> rt = cq.from(Reservas.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
