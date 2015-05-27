/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.andco.salasucc.controller;

import com.andco.salasucc.controller.exceptions.IllegalOrphanException;
import com.andco.salasucc.controller.exceptions.NonexistentEntityException;
import com.andco.salasucc.model.GruposProgramas;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.andco.salasucc.model.Programas;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author ANDCO
 */
public class GruposProgramasJpaController implements Serializable {

    public GruposProgramasJpaController() {
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("com.andco_salasucc_war_1.0-SNAPSHOTPU");
        this.emf = factory;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(GruposProgramas gruposProgramas) {
        if (gruposProgramas.getProgramasList() == null) {
            gruposProgramas.setProgramasList(new ArrayList<Programas>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Programas> attachedProgramasList = new ArrayList<Programas>();
            for (Programas programasListProgramasToAttach : gruposProgramas.getProgramasList()) {
                programasListProgramasToAttach = em.getReference(programasListProgramasToAttach.getClass(), programasListProgramasToAttach.getIdprograma());
                attachedProgramasList.add(programasListProgramasToAttach);
            }
            gruposProgramas.setProgramasList(attachedProgramasList);
            em.persist(gruposProgramas);
            for (Programas programasListProgramas : gruposProgramas.getProgramasList()) {
                GruposProgramas oldIdgrupoProgramasOfProgramasListProgramas = programasListProgramas.getIdgrupoProgramas();
                programasListProgramas.setIdgrupoProgramas(gruposProgramas);
                programasListProgramas = em.merge(programasListProgramas);
                if (oldIdgrupoProgramasOfProgramasListProgramas != null) {
                    oldIdgrupoProgramasOfProgramasListProgramas.getProgramasList().remove(programasListProgramas);
                    oldIdgrupoProgramasOfProgramasListProgramas = em.merge(oldIdgrupoProgramasOfProgramasListProgramas);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(GruposProgramas gruposProgramas) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            GruposProgramas persistentGruposProgramas = em.find(GruposProgramas.class, gruposProgramas.getIdgrupoProgramas());
            List<Programas> programasListOld = persistentGruposProgramas.getProgramasList();
            List<Programas> programasListNew = gruposProgramas.getProgramasList();
            List<String> illegalOrphanMessages = null;
            for (Programas programasListOldProgramas : programasListOld) {
                if (!programasListNew.contains(programasListOldProgramas)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Programas " + programasListOldProgramas + " since its idgrupoProgramas field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Programas> attachedProgramasListNew = new ArrayList<Programas>();
            for (Programas programasListNewProgramasToAttach : programasListNew) {
                programasListNewProgramasToAttach = em.getReference(programasListNewProgramasToAttach.getClass(), programasListNewProgramasToAttach.getIdprograma());
                attachedProgramasListNew.add(programasListNewProgramasToAttach);
            }
            programasListNew = attachedProgramasListNew;
            gruposProgramas.setProgramasList(programasListNew);
            gruposProgramas = em.merge(gruposProgramas);
            for (Programas programasListNewProgramas : programasListNew) {
                if (!programasListOld.contains(programasListNewProgramas)) {
                    GruposProgramas oldIdgrupoProgramasOfProgramasListNewProgramas = programasListNewProgramas.getIdgrupoProgramas();
                    programasListNewProgramas.setIdgrupoProgramas(gruposProgramas);
                    programasListNewProgramas = em.merge(programasListNewProgramas);
                    if (oldIdgrupoProgramasOfProgramasListNewProgramas != null && !oldIdgrupoProgramasOfProgramasListNewProgramas.equals(gruposProgramas)) {
                        oldIdgrupoProgramasOfProgramasListNewProgramas.getProgramasList().remove(programasListNewProgramas);
                        oldIdgrupoProgramasOfProgramasListNewProgramas = em.merge(oldIdgrupoProgramasOfProgramasListNewProgramas);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = gruposProgramas.getIdgrupoProgramas();
                if (findGruposProgramas(id) == null) {
                    throw new NonexistentEntityException("The gruposProgramas with id " + id + " no longer exists.");
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
            GruposProgramas gruposProgramas;
            try {
                gruposProgramas = em.getReference(GruposProgramas.class, id);
                gruposProgramas.getIdgrupoProgramas();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The gruposProgramas with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Programas> programasListOrphanCheck = gruposProgramas.getProgramasList();
            for (Programas programasListOrphanCheckProgramas : programasListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This GruposProgramas (" + gruposProgramas + ") cannot be destroyed since the Programas " + programasListOrphanCheckProgramas + " in its programasList field has a non-nullable idgrupoProgramas field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(gruposProgramas);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<GruposProgramas> findGruposProgramasEntitiesActivas() {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createNamedQuery("GruposProgramas.findByActivo", GruposProgramas.class).setParameter("activo", true);
            return q.getResultList();
        } finally {
            em.close();
        }
    }
    
    public List<GruposProgramas> findGruposProgramasEntities() {
        return findGruposProgramasEntities(true, -1, -1);
    }

    public List<GruposProgramas> findGruposProgramasEntities(int maxResults, int firstResult) {
        return findGruposProgramasEntities(false, maxResults, firstResult);
    }

    private List<GruposProgramas> findGruposProgramasEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(GruposProgramas.class));
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

    public GruposProgramas findGruposProgramas(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(GruposProgramas.class, id);
        } finally {
            em.close();
        }
    }

    public int getGruposProgramasCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<GruposProgramas> rt = cq.from(GruposProgramas.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}