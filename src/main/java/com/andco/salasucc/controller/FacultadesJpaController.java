/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.andco.salasucc.controller;

import com.andco.salasucc.controller.exceptions.NonexistentEntityException;
import com.andco.salasucc.model.Facultades;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.andco.salasucc.model.Usuarios;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author ANDCO
 */
public class FacultadesJpaController implements Serializable {

    public FacultadesJpaController() {
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("com.andco_salasucc_war_1.0-SNAPSHOTPU");
        this.emf = factory;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Facultades facultades) {
        if (facultades.getUsuariosList() == null) {
            facultades.setUsuariosList(new ArrayList<Usuarios>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Usuarios> attachedUsuariosList = new ArrayList<Usuarios>();
            for (Usuarios usuariosListUsuariosToAttach : facultades.getUsuariosList()) {
                usuariosListUsuariosToAttach = em.getReference(usuariosListUsuariosToAttach.getClass(), usuariosListUsuariosToAttach.getIdusuario());
                attachedUsuariosList.add(usuariosListUsuariosToAttach);
            }
            facultades.setUsuariosList(attachedUsuariosList);
            em.persist(facultades);
            for (Usuarios usuariosListUsuarios : facultades.getUsuariosList()) {
                Facultades oldIdfacultadOfUsuariosListUsuarios = usuariosListUsuarios.getIdfacultad();
                usuariosListUsuarios.setIdfacultad(facultades);
                usuariosListUsuarios = em.merge(usuariosListUsuarios);
                if (oldIdfacultadOfUsuariosListUsuarios != null) {
                    oldIdfacultadOfUsuariosListUsuarios.getUsuariosList().remove(usuariosListUsuarios);
                    oldIdfacultadOfUsuariosListUsuarios = em.merge(oldIdfacultadOfUsuariosListUsuarios);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Facultades facultades) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Facultades persistentFacultades = em.find(Facultades.class, facultades.getIdfacultad());
            List<Usuarios> usuariosListOld = persistentFacultades.getUsuariosList();
            List<Usuarios> usuariosListNew = facultades.getUsuariosList();
            List<Usuarios> attachedUsuariosListNew = new ArrayList<Usuarios>();
            for (Usuarios usuariosListNewUsuariosToAttach : usuariosListNew) {
                usuariosListNewUsuariosToAttach = em.getReference(usuariosListNewUsuariosToAttach.getClass(), usuariosListNewUsuariosToAttach.getIdusuario());
                attachedUsuariosListNew.add(usuariosListNewUsuariosToAttach);
            }
            usuariosListNew = attachedUsuariosListNew;
            facultades.setUsuariosList(usuariosListNew);
            facultades = em.merge(facultades);
            for (Usuarios usuariosListOldUsuarios : usuariosListOld) {
                if (!usuariosListNew.contains(usuariosListOldUsuarios)) {
                    usuariosListOldUsuarios.setIdfacultad(null);
                    usuariosListOldUsuarios = em.merge(usuariosListOldUsuarios);
                }
            }
            for (Usuarios usuariosListNewUsuarios : usuariosListNew) {
                if (!usuariosListOld.contains(usuariosListNewUsuarios)) {
                    Facultades oldIdfacultadOfUsuariosListNewUsuarios = usuariosListNewUsuarios.getIdfacultad();
                    usuariosListNewUsuarios.setIdfacultad(facultades);
                    usuariosListNewUsuarios = em.merge(usuariosListNewUsuarios);
                    if (oldIdfacultadOfUsuariosListNewUsuarios != null && !oldIdfacultadOfUsuariosListNewUsuarios.equals(facultades)) {
                        oldIdfacultadOfUsuariosListNewUsuarios.getUsuariosList().remove(usuariosListNewUsuarios);
                        oldIdfacultadOfUsuariosListNewUsuarios = em.merge(oldIdfacultadOfUsuariosListNewUsuarios);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = facultades.getIdfacultad();
                if (findFacultades(id) == null) {
                    throw new NonexistentEntityException("The facultades with id " + id + " no longer exists.");
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
            Facultades facultades;
            try {
                facultades = em.getReference(Facultades.class, id);
                facultades.getIdfacultad();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The facultades with id " + id + " no longer exists.", enfe);
            }
            List<Usuarios> usuariosList = facultades.getUsuariosList();
            for (Usuarios usuariosListUsuarios : usuariosList) {
                usuariosListUsuarios.setIdfacultad(null);
                usuariosListUsuarios = em.merge(usuariosListUsuarios);
            }
            em.remove(facultades);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Facultades> findFacultadesEntitiesActivas() {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createNamedQuery("Facultades.findByActivo", Facultades.class).setParameter("activo", true);
            return q.getResultList();
        } finally {
            em.close();
        }
    }
    
    public List<Facultades> findFacultadesEntities() {
        return findFacultadesEntities(true, -1, -1);
    }

    public List<Facultades> findFacultadesEntities(int maxResults, int firstResult) {
        return findFacultadesEntities(false, maxResults, firstResult);
    }

    private List<Facultades> findFacultadesEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Facultades.class));
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

    public Facultades findFacultades(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Facultades.class, id);
        } finally {
            em.close();
        }
    }

    public int getFacultadesCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Facultades> rt = cq.from(Facultades.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
