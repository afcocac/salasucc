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
import com.andco.salasucc.model.Facultades;
import com.andco.salasucc.model.Reservas;
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
public class UsuariosJpaController implements Serializable {

    public UsuariosJpaController() {
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("com.andco_salasucc_war_1.0-SNAPSHOTPU");
        this.emf = factory;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Usuarios usuarios) {
        if (usuarios.getReservasList() == null) {
            usuarios.setReservasList(new ArrayList<Reservas>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Facultades idfacultad = usuarios.getIdfacultad();
            if (idfacultad != null) {
                idfacultad = em.getReference(idfacultad.getClass(), idfacultad.getIdfacultad());
                usuarios.setIdfacultad(idfacultad);
            }
            List<Reservas> attachedReservasList = new ArrayList<Reservas>();
            for (Reservas reservasListReservasToAttach : usuarios.getReservasList()) {
                reservasListReservasToAttach = em.getReference(reservasListReservasToAttach.getClass(), reservasListReservasToAttach.getIdreserva());
                attachedReservasList.add(reservasListReservasToAttach);
            }
            usuarios.setReservasList(attachedReservasList);
            em.persist(usuarios);
            if (idfacultad != null) {
                idfacultad.getUsuariosList().add(usuarios);
                idfacultad = em.merge(idfacultad);
            }
            for (Reservas reservasListReservas : usuarios.getReservasList()) {
                Usuarios oldIdusuarioOfReservasListReservas = reservasListReservas.getIdusuario();
                reservasListReservas.setIdusuario(usuarios);
                reservasListReservas = em.merge(reservasListReservas);
                if (oldIdusuarioOfReservasListReservas != null) {
                    oldIdusuarioOfReservasListReservas.getReservasList().remove(reservasListReservas);
                    oldIdusuarioOfReservasListReservas = em.merge(oldIdusuarioOfReservasListReservas);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Usuarios usuarios) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Usuarios persistentUsuarios = em.find(Usuarios.class, usuarios.getIdusuario());
            Facultades idfacultadOld = persistentUsuarios.getIdfacultad();
            Facultades idfacultadNew = usuarios.getIdfacultad();
            List<Reservas> reservasListOld = persistentUsuarios.getReservasList();
            List<Reservas> reservasListNew = usuarios.getReservasList();
            List<String> illegalOrphanMessages = null;
            for (Reservas reservasListOldReservas : reservasListOld) {
                if (!reservasListNew.contains(reservasListOldReservas)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Reservas " + reservasListOldReservas + " since its idusuario field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (idfacultadNew != null) {
                idfacultadNew = em.getReference(idfacultadNew.getClass(), idfacultadNew.getIdfacultad());
                usuarios.setIdfacultad(idfacultadNew);
            }
            List<Reservas> attachedReservasListNew = new ArrayList<Reservas>();
            for (Reservas reservasListNewReservasToAttach : reservasListNew) {
                reservasListNewReservasToAttach = em.getReference(reservasListNewReservasToAttach.getClass(), reservasListNewReservasToAttach.getIdreserva());
                attachedReservasListNew.add(reservasListNewReservasToAttach);
            }
            reservasListNew = attachedReservasListNew;
            usuarios.setReservasList(reservasListNew);
            usuarios = em.merge(usuarios);
            if (idfacultadOld != null && !idfacultadOld.equals(idfacultadNew)) {
                idfacultadOld.getUsuariosList().remove(usuarios);
                idfacultadOld = em.merge(idfacultadOld);
            }
            if (idfacultadNew != null && !idfacultadNew.equals(idfacultadOld)) {
                idfacultadNew.getUsuariosList().add(usuarios);
                idfacultadNew = em.merge(idfacultadNew);
            }
            for (Reservas reservasListNewReservas : reservasListNew) {
                if (!reservasListOld.contains(reservasListNewReservas)) {
                    Usuarios oldIdusuarioOfReservasListNewReservas = reservasListNewReservas.getIdusuario();
                    reservasListNewReservas.setIdusuario(usuarios);
                    reservasListNewReservas = em.merge(reservasListNewReservas);
                    if (oldIdusuarioOfReservasListNewReservas != null && !oldIdusuarioOfReservasListNewReservas.equals(usuarios)) {
                        oldIdusuarioOfReservasListNewReservas.getReservasList().remove(reservasListNewReservas);
                        oldIdusuarioOfReservasListNewReservas = em.merge(oldIdusuarioOfReservasListNewReservas);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = usuarios.getIdusuario();
                if (findUsuarios(id) == null) {
                    throw new NonexistentEntityException("The usuarios with id " + id + " no longer exists.");
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
            Usuarios usuarios;
            try {
                usuarios = em.getReference(Usuarios.class, id);
                usuarios.getIdusuario();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The usuarios with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Reservas> reservasListOrphanCheck = usuarios.getReservasList();
            for (Reservas reservasListOrphanCheckReservas : reservasListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Usuarios (" + usuarios + ") cannot be destroyed since the Reservas " + reservasListOrphanCheckReservas + " in its reservasList field has a non-nullable idusuario field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Facultades idfacultad = usuarios.getIdfacultad();
            if (idfacultad != null) {
                idfacultad.getUsuariosList().remove(usuarios);
                idfacultad = em.merge(idfacultad);
            }
            em.remove(usuarios);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }
    
    public Usuarios findUsuarioByCorreo(String correo) {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createNamedQuery("Usuarios.findByCorreo", Usuarios.class).setParameter("correo", correo);
            return (Usuarios) q.getSingleResult();
        } catch (Exception e) {
            return null;
        } finally {
            em.close();
        }
    }

    public List<Usuarios> findUsuariosEntities() {
        return findUsuariosEntities(true, -1, -1);
    }

    public List<Usuarios> findUsuariosEntities(int maxResults, int firstResult) {
        return findUsuariosEntities(false, maxResults, firstResult);
    }

    private List<Usuarios> findUsuariosEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Usuarios.class));
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

    public Usuarios findUsuarios(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Usuarios.class, id);
        } finally {
            em.close();
        }
    }

    public int getUsuariosCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Usuarios> rt = cq.from(Usuarios.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
