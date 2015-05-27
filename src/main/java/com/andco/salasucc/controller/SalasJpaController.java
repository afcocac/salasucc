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
import com.andco.salasucc.model.SalasHasProgramas;
import java.util.ArrayList;
import java.util.List;
import com.andco.salasucc.model.Reservas;
import com.andco.salasucc.model.Salas;
import java.util.Date;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author ANDCO
 */
public class SalasJpaController implements Serializable {

    public SalasJpaController() {
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("com.andco_salasucc_war_1.0-SNAPSHOTPU");
        this.emf = factory;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Salas salas) {
        if (salas.getSalasHasProgramasList() == null) {
            salas.setSalasHasProgramasList(new ArrayList<SalasHasProgramas>());
        }
        if (salas.getReservasList() == null) {
            salas.setReservasList(new ArrayList<Reservas>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<SalasHasProgramas> attachedSalasHasProgramasList = new ArrayList<SalasHasProgramas>();
            for (SalasHasProgramas salasHasProgramasListSalasHasProgramasToAttach : salas.getSalasHasProgramasList()) {
                salasHasProgramasListSalasHasProgramasToAttach = em.getReference(salasHasProgramasListSalasHasProgramasToAttach.getClass(), salasHasProgramasListSalasHasProgramasToAttach.getSalasHasProgramasPK());
                attachedSalasHasProgramasList.add(salasHasProgramasListSalasHasProgramasToAttach);
            }
            salas.setSalasHasProgramasList(attachedSalasHasProgramasList);
            List<Reservas> attachedReservasList = new ArrayList<Reservas>();
            for (Reservas reservasListReservasToAttach : salas.getReservasList()) {
                reservasListReservasToAttach = em.getReference(reservasListReservasToAttach.getClass(), reservasListReservasToAttach.getIdreserva());
                attachedReservasList.add(reservasListReservasToAttach);
            }
            salas.setReservasList(attachedReservasList);
            em.persist(salas);
            for (SalasHasProgramas salasHasProgramasListSalasHasProgramas : salas.getSalasHasProgramasList()) {
                Salas oldSalasOfSalasHasProgramasListSalasHasProgramas = salasHasProgramasListSalasHasProgramas.getSalas();
                salasHasProgramasListSalasHasProgramas.setSalas(salas);
                salasHasProgramasListSalasHasProgramas = em.merge(salasHasProgramasListSalasHasProgramas);
                if (oldSalasOfSalasHasProgramasListSalasHasProgramas != null) {
                    oldSalasOfSalasHasProgramasListSalasHasProgramas.getSalasHasProgramasList().remove(salasHasProgramasListSalasHasProgramas);
                    oldSalasOfSalasHasProgramasListSalasHasProgramas = em.merge(oldSalasOfSalasHasProgramasListSalasHasProgramas);
                }
            }
            for (Reservas reservasListReservas : salas.getReservasList()) {
                Salas oldIdsalaOfReservasListReservas = reservasListReservas.getIdsala();
                reservasListReservas.setIdsala(salas);
                reservasListReservas = em.merge(reservasListReservas);
                if (oldIdsalaOfReservasListReservas != null) {
                    oldIdsalaOfReservasListReservas.getReservasList().remove(reservasListReservas);
                    oldIdsalaOfReservasListReservas = em.merge(oldIdsalaOfReservasListReservas);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Salas salas) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Salas persistentSalas = em.find(Salas.class, salas.getIdsala());
            List<SalasHasProgramas> salasHasProgramasListOld = persistentSalas.getSalasHasProgramasList();
            List<SalasHasProgramas> salasHasProgramasListNew = salas.getSalasHasProgramasList();
            List<Reservas> reservasListOld = persistentSalas.getReservasList();
            List<Reservas> reservasListNew = salas.getReservasList();
            List<String> illegalOrphanMessages = null;
            for (SalasHasProgramas salasHasProgramasListOldSalasHasProgramas : salasHasProgramasListOld) {
                if (!salasHasProgramasListNew.contains(salasHasProgramasListOldSalasHasProgramas)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain SalasHasProgramas " + salasHasProgramasListOldSalasHasProgramas + " since its salas field is not nullable.");
                }
            }
            for (Reservas reservasListOldReservas : reservasListOld) {
                if (!reservasListNew.contains(reservasListOldReservas)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Reservas " + reservasListOldReservas + " since its idsala field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<SalasHasProgramas> attachedSalasHasProgramasListNew = new ArrayList<SalasHasProgramas>();
            for (SalasHasProgramas salasHasProgramasListNewSalasHasProgramasToAttach : salasHasProgramasListNew) {
                salasHasProgramasListNewSalasHasProgramasToAttach = em.getReference(salasHasProgramasListNewSalasHasProgramasToAttach.getClass(), salasHasProgramasListNewSalasHasProgramasToAttach.getSalasHasProgramasPK());
                attachedSalasHasProgramasListNew.add(salasHasProgramasListNewSalasHasProgramasToAttach);
            }
            salasHasProgramasListNew = attachedSalasHasProgramasListNew;
            salas.setSalasHasProgramasList(salasHasProgramasListNew);
            List<Reservas> attachedReservasListNew = new ArrayList<Reservas>();
            for (Reservas reservasListNewReservasToAttach : reservasListNew) {
                reservasListNewReservasToAttach = em.getReference(reservasListNewReservasToAttach.getClass(), reservasListNewReservasToAttach.getIdreserva());
                attachedReservasListNew.add(reservasListNewReservasToAttach);
            }
            reservasListNew = attachedReservasListNew;
            salas.setReservasList(reservasListNew);
            salas = em.merge(salas);
            for (SalasHasProgramas salasHasProgramasListNewSalasHasProgramas : salasHasProgramasListNew) {
                if (!salasHasProgramasListOld.contains(salasHasProgramasListNewSalasHasProgramas)) {
                    Salas oldSalasOfSalasHasProgramasListNewSalasHasProgramas = salasHasProgramasListNewSalasHasProgramas.getSalas();
                    salasHasProgramasListNewSalasHasProgramas.setSalas(salas);
                    salasHasProgramasListNewSalasHasProgramas = em.merge(salasHasProgramasListNewSalasHasProgramas);
                    if (oldSalasOfSalasHasProgramasListNewSalasHasProgramas != null && !oldSalasOfSalasHasProgramasListNewSalasHasProgramas.equals(salas)) {
                        oldSalasOfSalasHasProgramasListNewSalasHasProgramas.getSalasHasProgramasList().remove(salasHasProgramasListNewSalasHasProgramas);
                        oldSalasOfSalasHasProgramasListNewSalasHasProgramas = em.merge(oldSalasOfSalasHasProgramasListNewSalasHasProgramas);
                    }
                }
            }
            for (Reservas reservasListNewReservas : reservasListNew) {
                if (!reservasListOld.contains(reservasListNewReservas)) {
                    Salas oldIdsalaOfReservasListNewReservas = reservasListNewReservas.getIdsala();
                    reservasListNewReservas.setIdsala(salas);
                    reservasListNewReservas = em.merge(reservasListNewReservas);
                    if (oldIdsalaOfReservasListNewReservas != null && !oldIdsalaOfReservasListNewReservas.equals(salas)) {
                        oldIdsalaOfReservasListNewReservas.getReservasList().remove(reservasListNewReservas);
                        oldIdsalaOfReservasListNewReservas = em.merge(oldIdsalaOfReservasListNewReservas);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = salas.getIdsala();
                if (findSalas(id) == null) {
                    throw new NonexistentEntityException("The salas with id " + id + " no longer exists.");
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
            Salas salas;
            try {
                salas = em.getReference(Salas.class, id);
                salas.getIdsala();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The salas with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<SalasHasProgramas> salasHasProgramasListOrphanCheck = salas.getSalasHasProgramasList();
            for (SalasHasProgramas salasHasProgramasListOrphanCheckSalasHasProgramas : salasHasProgramasListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Salas (" + salas + ") cannot be destroyed since the SalasHasProgramas " + salasHasProgramasListOrphanCheckSalasHasProgramas + " in its salasHasProgramasList field has a non-nullable salas field.");
            }
            List<Reservas> reservasListOrphanCheck = salas.getReservasList();
            for (Reservas reservasListOrphanCheckReservas : reservasListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Salas (" + salas + ") cannot be destroyed since the Reservas " + reservasListOrphanCheckReservas + " in its reservasList field has a non-nullable idsala field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(salas);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }
    
    public List<Salas> buscarSalasDisponibles(int numEst, int idPrograma, Date fechaInicio, Date fechaFin) {
        EntityManager em = getEntityManager();
        try {
            String consulta = "SELECT s FROM Salas s, SalasHasProgramas p "
                    + "WHERE s.idsala = p.salasHasProgramasPK.idsala "
                    + "AND :numEst <= s.capMax "
                    + "AND :idPrograma = p.salasHasProgramasPK.idprograma "
                    + "AND s.activo = true "
                    + "AND s.idsala "
                    + "NOT IN "
                    + "(SELECT DISTINCT r.idsala.idsala "
                    + "FROM Reservas r "
                    + "WHERE r.fechaInicio < :fechaFin "
                    + "AND r.fechaFin > :fechaInicio )";
            Query q = em.createQuery(consulta);
            q.setParameter("numEst", numEst);
            q.setParameter("idPrograma", idPrograma);
            q.setParameter("fechaInicio", fechaInicio);
            q.setParameter("fechaFin", fechaFin);
            return q.getResultList();
        } finally {
            em.close();
        }
    }
    
    public List<Salas> buscarSalasDisponibles(int numEst, Date fechaInicio, Date fechaFin) {
        EntityManager em = getEntityManager();
        try {
            String consulta = "SELECT s FROM Salas s, SalasHasProgramas p "
                    + "WHERE s.idsala = p.salasHasProgramasPK.idsala "
                    + "AND :numEst <= s.capMax "
                    + "AND s.activo = true "
                    + "AND s.idsala "
                    + "NOT IN "
                    + "(SELECT DISTINCT r.idsala.idsala "
                    + "FROM Reservas r "
                    + "WHERE r.fechaInicio < :fechaFin "
                    + "AND r.fechaFin > :fechaInicio )";
            Query q = em.createQuery(consulta);
            q.setParameter("numEst", numEst);
            q.setParameter("fechaInicio", fechaInicio);
            q.setParameter("fechaFin", fechaFin);
            return q.getResultList();
        } finally {
            em.close();
        }
    }
    
    public List<Salas> findSalasEntitiesActivas() {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createNamedQuery("Salas.findByActivo", Salas.class).setParameter("activo", true);
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public List<Salas> findSalasEntities() {
        return findSalasEntities(true, -1, -1);
    }

    public List<Salas> findSalasEntities(int maxResults, int firstResult) {
        return findSalasEntities(false, maxResults, firstResult);
    }

    private List<Salas> findSalasEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Salas.class));
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

    public Salas findSalas(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Salas.class, id);
        } finally {
            em.close();
        }
    }

    public int getSalasCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Salas> rt = cq.from(Salas.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}