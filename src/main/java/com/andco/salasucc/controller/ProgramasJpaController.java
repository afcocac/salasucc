/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.andco.salasucc.controller;

import com.andco.salasucc.controller.exceptions.IllegalOrphanException;
import com.andco.salasucc.controller.exceptions.NonexistentEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.andco.salasucc.model.GruposProgramas;
import com.andco.salasucc.model.Programas;
import com.andco.salasucc.model.SalasHasProgramas;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author ANDCO
 */
public class ProgramasJpaController implements Serializable {

    public ProgramasJpaController() {
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("com.andco_salasucc_war_1.0-SNAPSHOTPU");
        this.emf = factory;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Programas programas) {
        if (programas.getSalasHasProgramasList() == null) {
            programas.setSalasHasProgramasList(new ArrayList<SalasHasProgramas>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            GruposProgramas idgrupoProgramas = programas.getIdgrupoProgramas();
            if (idgrupoProgramas != null) {
                idgrupoProgramas = em.getReference(idgrupoProgramas.getClass(), idgrupoProgramas.getIdgrupoProgramas());
                programas.setIdgrupoProgramas(idgrupoProgramas);
            }
            List<SalasHasProgramas> attachedSalasHasProgramasList = new ArrayList<SalasHasProgramas>();
            for (SalasHasProgramas salasHasProgramasListSalasHasProgramasToAttach : programas.getSalasHasProgramasList()) {
                salasHasProgramasListSalasHasProgramasToAttach = em.getReference(salasHasProgramasListSalasHasProgramasToAttach.getClass(), salasHasProgramasListSalasHasProgramasToAttach.getSalasHasProgramasPK());
                attachedSalasHasProgramasList.add(salasHasProgramasListSalasHasProgramasToAttach);
            }
            programas.setSalasHasProgramasList(attachedSalasHasProgramasList);
            em.persist(programas);
            if (idgrupoProgramas != null) {
                idgrupoProgramas.getProgramasList().add(programas);
                idgrupoProgramas = em.merge(idgrupoProgramas);
            }
            for (SalasHasProgramas salasHasProgramasListSalasHasProgramas : programas.getSalasHasProgramasList()) {
                Programas oldProgramasOfSalasHasProgramasListSalasHasProgramas = salasHasProgramasListSalasHasProgramas.getProgramas();
                salasHasProgramasListSalasHasProgramas.setProgramas(programas);
                salasHasProgramasListSalasHasProgramas = em.merge(salasHasProgramasListSalasHasProgramas);
                if (oldProgramasOfSalasHasProgramasListSalasHasProgramas != null) {
                    oldProgramasOfSalasHasProgramasListSalasHasProgramas.getSalasHasProgramasList().remove(salasHasProgramasListSalasHasProgramas);
                    oldProgramasOfSalasHasProgramasListSalasHasProgramas = em.merge(oldProgramasOfSalasHasProgramasListSalasHasProgramas);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Programas programas) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Programas persistentProgramas = em.find(Programas.class, programas.getIdprograma());
            GruposProgramas idgrupoProgramasOld = persistentProgramas.getIdgrupoProgramas();
            GruposProgramas idgrupoProgramasNew = programas.getIdgrupoProgramas();
            List<SalasHasProgramas> salasHasProgramasListOld = persistentProgramas.getSalasHasProgramasList();
            List<SalasHasProgramas> salasHasProgramasListNew = programas.getSalasHasProgramasList();
            List<String> illegalOrphanMessages = null;
            for (SalasHasProgramas salasHasProgramasListOldSalasHasProgramas : salasHasProgramasListOld) {
                if (!salasHasProgramasListNew.contains(salasHasProgramasListOldSalasHasProgramas)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain SalasHasProgramas " + salasHasProgramasListOldSalasHasProgramas + " since its programas field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (idgrupoProgramasNew != null) {
                idgrupoProgramasNew = em.getReference(idgrupoProgramasNew.getClass(), idgrupoProgramasNew.getIdgrupoProgramas());
                programas.setIdgrupoProgramas(idgrupoProgramasNew);
            }
            List<SalasHasProgramas> attachedSalasHasProgramasListNew = new ArrayList<SalasHasProgramas>();
            for (SalasHasProgramas salasHasProgramasListNewSalasHasProgramasToAttach : salasHasProgramasListNew) {
                salasHasProgramasListNewSalasHasProgramasToAttach = em.getReference(salasHasProgramasListNewSalasHasProgramasToAttach.getClass(), salasHasProgramasListNewSalasHasProgramasToAttach.getSalasHasProgramasPK());
                attachedSalasHasProgramasListNew.add(salasHasProgramasListNewSalasHasProgramasToAttach);
            }
            salasHasProgramasListNew = attachedSalasHasProgramasListNew;
            programas.setSalasHasProgramasList(salasHasProgramasListNew);
            programas = em.merge(programas);
            if (idgrupoProgramasOld != null && !idgrupoProgramasOld.equals(idgrupoProgramasNew)) {
                idgrupoProgramasOld.getProgramasList().remove(programas);
                idgrupoProgramasOld = em.merge(idgrupoProgramasOld);
            }
            if (idgrupoProgramasNew != null && !idgrupoProgramasNew.equals(idgrupoProgramasOld)) {
                idgrupoProgramasNew.getProgramasList().add(programas);
                idgrupoProgramasNew = em.merge(idgrupoProgramasNew);
            }
            for (SalasHasProgramas salasHasProgramasListNewSalasHasProgramas : salasHasProgramasListNew) {
                if (!salasHasProgramasListOld.contains(salasHasProgramasListNewSalasHasProgramas)) {
                    Programas oldProgramasOfSalasHasProgramasListNewSalasHasProgramas = salasHasProgramasListNewSalasHasProgramas.getProgramas();
                    salasHasProgramasListNewSalasHasProgramas.setProgramas(programas);
                    salasHasProgramasListNewSalasHasProgramas = em.merge(salasHasProgramasListNewSalasHasProgramas);
                    if (oldProgramasOfSalasHasProgramasListNewSalasHasProgramas != null && !oldProgramasOfSalasHasProgramasListNewSalasHasProgramas.equals(programas)) {
                        oldProgramasOfSalasHasProgramasListNewSalasHasProgramas.getSalasHasProgramasList().remove(salasHasProgramasListNewSalasHasProgramas);
                        oldProgramasOfSalasHasProgramasListNewSalasHasProgramas = em.merge(oldProgramasOfSalasHasProgramasListNewSalasHasProgramas);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = programas.getIdprograma();
                if (findProgramas(id) == null) {
                    throw new NonexistentEntityException("The programas with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Programas programas;
            try {
                programas = em.getReference(Programas.class, id);
                programas.getIdprograma();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The programas with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<SalasHasProgramas> salasHasProgramasListOrphanCheck = programas.getSalasHasProgramasList();
            for (SalasHasProgramas salasHasProgramasListOrphanCheckSalasHasProgramas : salasHasProgramasListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Programas (" + programas + ") cannot be destroyed since the SalasHasProgramas " + salasHasProgramasListOrphanCheckSalasHasProgramas + " in its salasHasProgramasList field has a non-nullable programas field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            GruposProgramas idgrupoProgramas = programas.getIdgrupoProgramas();
            if (idgrupoProgramas != null) {
                idgrupoProgramas.getProgramasList().remove(programas);
                idgrupoProgramas = em.merge(idgrupoProgramas);
            }
            em.remove(programas);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Programas> findProgramasEntities() {
        return findProgramasEntities(true, -1, -1);
    }

    public List<Programas> findProgramasEntities(int maxResults, int firstResult) {
        return findProgramasEntities(false, maxResults, firstResult);
    }

    private List<Programas> findProgramasEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Programas.class));
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

    public Programas findProgramas(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Programas.class, id);
        } finally {
            em.close();
        }
    }

    public int getProgramasCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Programas> rt = cq.from(Programas.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
