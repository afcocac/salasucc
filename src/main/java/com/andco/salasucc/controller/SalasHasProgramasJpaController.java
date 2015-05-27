/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.andco.salasucc.controller;

import com.andco.salasucc.controller.exceptions.NonexistentEntityException;
import com.andco.salasucc.controller.exceptions.PreexistingEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.andco.salasucc.model.Programas;
import com.andco.salasucc.model.Salas;
import com.andco.salasucc.model.SalasHasProgramas;
import com.andco.salasucc.model.SalasHasProgramasPK;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author ANDCO
 */
public class SalasHasProgramasJpaController implements Serializable {

    public SalasHasProgramasJpaController() {
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("com.andco_salasucc_war_1.0-SNAPSHOTPU");
        this.emf = factory;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(SalasHasProgramas salasHasProgramas) throws PreexistingEntityException, Exception {
        if (salasHasProgramas.getSalasHasProgramasPK() == null) {
            salasHasProgramas.setSalasHasProgramasPK(new SalasHasProgramasPK());
        }
        salasHasProgramas.getSalasHasProgramasPK().setIdprograma(salasHasProgramas.getProgramas().getIdprograma());
        salasHasProgramas.getSalasHasProgramasPK().setIdsala(salasHasProgramas.getSalas().getIdsala());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Programas programas = salasHasProgramas.getProgramas();
            if (programas != null) {
                programas = em.getReference(programas.getClass(), programas.getIdprograma());
                salasHasProgramas.setProgramas(programas);
            }
            Salas salas = salasHasProgramas.getSalas();
            if (salas != null) {
                salas = em.getReference(salas.getClass(), salas.getIdsala());
                salasHasProgramas.setSalas(salas);
            }
            em.persist(salasHasProgramas);
            if (programas != null) {
                programas.getSalasHasProgramasList().add(salasHasProgramas);
                programas = em.merge(programas);
            }
            if (salas != null) {
                salas.getSalasHasProgramasList().add(salasHasProgramas);
                salas = em.merge(salas);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findSalasHasProgramas(salasHasProgramas.getSalasHasProgramasPK()) != null) {
                throw new PreexistingEntityException("SalasHasProgramas " + salasHasProgramas + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(SalasHasProgramas salasHasProgramas) throws NonexistentEntityException, Exception {
        salasHasProgramas.getSalasHasProgramasPK().setIdprograma(salasHasProgramas.getProgramas().getIdprograma());
        salasHasProgramas.getSalasHasProgramasPK().setIdsala(salasHasProgramas.getSalas().getIdsala());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            SalasHasProgramas persistentSalasHasProgramas = em.find(SalasHasProgramas.class, salasHasProgramas.getSalasHasProgramasPK());
            Programas programasOld = persistentSalasHasProgramas.getProgramas();
            Programas programasNew = salasHasProgramas.getProgramas();
            Salas salasOld = persistentSalasHasProgramas.getSalas();
            Salas salasNew = salasHasProgramas.getSalas();
            if (programasNew != null) {
                programasNew = em.getReference(programasNew.getClass(), programasNew.getIdprograma());
                salasHasProgramas.setProgramas(programasNew);
            }
            if (salasNew != null) {
                salasNew = em.getReference(salasNew.getClass(), salasNew.getIdsala());
                salasHasProgramas.setSalas(salasNew);
            }
            salasHasProgramas = em.merge(salasHasProgramas);
            if (programasOld != null && !programasOld.equals(programasNew)) {
                programasOld.getSalasHasProgramasList().remove(salasHasProgramas);
                programasOld = em.merge(programasOld);
            }
            if (programasNew != null && !programasNew.equals(programasOld)) {
                programasNew.getSalasHasProgramasList().add(salasHasProgramas);
                programasNew = em.merge(programasNew);
            }
            if (salasOld != null && !salasOld.equals(salasNew)) {
                salasOld.getSalasHasProgramasList().remove(salasHasProgramas);
                salasOld = em.merge(salasOld);
            }
            if (salasNew != null && !salasNew.equals(salasOld)) {
                salasNew.getSalasHasProgramasList().add(salasHasProgramas);
                salasNew = em.merge(salasNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                SalasHasProgramasPK id = salasHasProgramas.getSalasHasProgramasPK();
                if (findSalasHasProgramas(id) == null) {
                    throw new NonexistentEntityException("The salasHasProgramas with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(SalasHasProgramasPK id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            SalasHasProgramas salasHasProgramas;
            try {
                salasHasProgramas = em.getReference(SalasHasProgramas.class, id);
                salasHasProgramas.getSalasHasProgramasPK();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The salasHasProgramas with id " + id + " no longer exists.", enfe);
            }
            Programas programas = salasHasProgramas.getProgramas();
            if (programas != null) {
                programas.getSalasHasProgramasList().remove(salasHasProgramas);
                programas = em.merge(programas);
            }
            Salas salas = salasHasProgramas.getSalas();
            if (salas != null) {
                salas.getSalasHasProgramasList().remove(salasHasProgramas);
                salas = em.merge(salas);
            }
            em.remove(salasHasProgramas);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<SalasHasProgramas> findSalasHasProgramasEntities() {
        return findSalasHasProgramasEntities(true, -1, -1);
    }

    public List<SalasHasProgramas> findSalasHasProgramasEntities(int maxResults, int firstResult) {
        return findSalasHasProgramasEntities(false, maxResults, firstResult);
    }

    private List<SalasHasProgramas> findSalasHasProgramasEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(SalasHasProgramas.class));
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

    public SalasHasProgramas findSalasHasProgramas(SalasHasProgramasPK id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(SalasHasProgramas.class, id);
        } finally {
            em.close();
        }
    }

    public int getSalasHasProgramasCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<SalasHasProgramas> rt = cq.from(SalasHasProgramas.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
