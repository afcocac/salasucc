/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.andco.salasucc.controller;

import com.andco.salasucc.controller.exceptions.NonexistentEntityException;
import com.andco.salasucc.model.MaterialesAdicionales;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.andco.salasucc.model.Reservas;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author ANDCO
 */
public class MaterialesAdicionalesJpaController implements Serializable {

    public MaterialesAdicionalesJpaController() {
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("com.andco_salasucc_war_1.0-SNAPSHOTPU");
        this.emf = factory;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(MaterialesAdicionales materialesAdicionales) {
        if (materialesAdicionales.getReservasList() == null) {
            materialesAdicionales.setReservasList(new ArrayList<Reservas>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Reservas> attachedReservasList = new ArrayList<Reservas>();
            for (Reservas reservasListReservasToAttach : materialesAdicionales.getReservasList()) {
                reservasListReservasToAttach = em.getReference(reservasListReservasToAttach.getClass(), reservasListReservasToAttach.getIdreserva());
                attachedReservasList.add(reservasListReservasToAttach);
            }
            materialesAdicionales.setReservasList(attachedReservasList);
            em.persist(materialesAdicionales);
            for (Reservas reservasListReservas : materialesAdicionales.getReservasList()) {
                MaterialesAdicionales oldIdmaterialAdicionalOfReservasListReservas = reservasListReservas.getIdmaterialAdicional();
                reservasListReservas.setIdmaterialAdicional(materialesAdicionales);
                reservasListReservas = em.merge(reservasListReservas);
                if (oldIdmaterialAdicionalOfReservasListReservas != null) {
                    oldIdmaterialAdicionalOfReservasListReservas.getReservasList().remove(reservasListReservas);
                    oldIdmaterialAdicionalOfReservasListReservas = em.merge(oldIdmaterialAdicionalOfReservasListReservas);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(MaterialesAdicionales materialesAdicionales) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            MaterialesAdicionales persistentMaterialesAdicionales = em.find(MaterialesAdicionales.class, materialesAdicionales.getIdmaterialAdicional());
            List<Reservas> reservasListOld = persistentMaterialesAdicionales.getReservasList();
            List<Reservas> reservasListNew = materialesAdicionales.getReservasList();
            List<Reservas> attachedReservasListNew = new ArrayList<Reservas>();
            for (Reservas reservasListNewReservasToAttach : reservasListNew) {
                reservasListNewReservasToAttach = em.getReference(reservasListNewReservasToAttach.getClass(), reservasListNewReservasToAttach.getIdreserva());
                attachedReservasListNew.add(reservasListNewReservasToAttach);
            }
            reservasListNew = attachedReservasListNew;
            materialesAdicionales.setReservasList(reservasListNew);
            materialesAdicionales = em.merge(materialesAdicionales);
            for (Reservas reservasListOldReservas : reservasListOld) {
                if (!reservasListNew.contains(reservasListOldReservas)) {
                    reservasListOldReservas.setIdmaterialAdicional(null);
                    reservasListOldReservas = em.merge(reservasListOldReservas);
                }
            }
            for (Reservas reservasListNewReservas : reservasListNew) {
                if (!reservasListOld.contains(reservasListNewReservas)) {
                    MaterialesAdicionales oldIdmaterialAdicionalOfReservasListNewReservas = reservasListNewReservas.getIdmaterialAdicional();
                    reservasListNewReservas.setIdmaterialAdicional(materialesAdicionales);
                    reservasListNewReservas = em.merge(reservasListNewReservas);
                    if (oldIdmaterialAdicionalOfReservasListNewReservas != null && !oldIdmaterialAdicionalOfReservasListNewReservas.equals(materialesAdicionales)) {
                        oldIdmaterialAdicionalOfReservasListNewReservas.getReservasList().remove(reservasListNewReservas);
                        oldIdmaterialAdicionalOfReservasListNewReservas = em.merge(oldIdmaterialAdicionalOfReservasListNewReservas);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = materialesAdicionales.getIdmaterialAdicional();
                if (findMaterialesAdicionales(id) == null) {
                    throw new NonexistentEntityException("The materialesAdicionales with id " + id + " no longer exists.");
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
            MaterialesAdicionales materialesAdicionales;
            try {
                materialesAdicionales = em.getReference(MaterialesAdicionales.class, id);
                materialesAdicionales.getIdmaterialAdicional();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The materialesAdicionales with id " + id + " no longer exists.", enfe);
            }
            List<Reservas> reservasList = materialesAdicionales.getReservasList();
            for (Reservas reservasListReservas : reservasList) {
                reservasListReservas.setIdmaterialAdicional(null);
                reservasListReservas = em.merge(reservasListReservas);
            }
            em.remove(materialesAdicionales);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<MaterialesAdicionales> findMaterialesAdicionalesEntitiesActivas() {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createNamedQuery("MaterialesAdicionales.findByActivo", MaterialesAdicionales.class).setParameter("activo", true);
            return q.getResultList();
        } finally {
            em.close();
        }
    }    
    
    public List<MaterialesAdicionales> findMaterialesAdicionalesEntities() {
        return findMaterialesAdicionalesEntities(true, -1, -1);
    }

    public List<MaterialesAdicionales> findMaterialesAdicionalesEntities(int maxResults, int firstResult) {
        return findMaterialesAdicionalesEntities(false, maxResults, firstResult);
    }

    private List<MaterialesAdicionales> findMaterialesAdicionalesEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(MaterialesAdicionales.class));
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

    public MaterialesAdicionales findMaterialesAdicionales(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(MaterialesAdicionales.class, id);
        } finally {
            em.close();
        }
    }

    public int getMaterialesAdicionalesCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<MaterialesAdicionales> rt = cq.from(MaterialesAdicionales.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}