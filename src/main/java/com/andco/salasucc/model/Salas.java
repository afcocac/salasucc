/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.andco.salasucc.model;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import org.primefaces.model.DefaultScheduleEvent;
import org.primefaces.model.DefaultScheduleModel;
import org.primefaces.model.ScheduleModel;

/**
 *
 * @author ANDCO
 */
@Entity
@Table(name = "salas")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Salas.findAll", query = "SELECT s FROM Salas s"),
    @NamedQuery(name = "Salas.findByIdsala", query = "SELECT s FROM Salas s WHERE s.idsala = :idsala"),
    @NamedQuery(name = "Salas.findByNombre", query = "SELECT s FROM Salas s WHERE s.nombre = :nombre"),
    @NamedQuery(name = "Salas.findByNumEquipos", query = "SELECT s FROM Salas s WHERE s.numEquipos = :numEquipos"),
    @NamedQuery(name = "Salas.findByCapMin", query = "SELECT s FROM Salas s WHERE s.capMin = :capMin"),
    @NamedQuery(name = "Salas.findByCapMax", query = "SELECT s FROM Salas s WHERE s.capMax = :capMax"),
    @NamedQuery(name = "Salas.findByActivo", query = "SELECT s FROM Salas s WHERE s.activo = :activo")})
public class Salas implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idsala")
    private Integer idsala;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "nombre")
    private String nombre;
    @Column(name = "num_equipos")
    private Integer numEquipos;
    @Basic(optional = false)
    @NotNull
    @Column(name = "cap_min")
    private int capMin;
    @Basic(optional = false)
    @NotNull
    @Column(name = "cap_max")
    private int capMax;
    @Basic(optional = false)
    @NotNull
    @Column(name = "activo")
    private boolean activo;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "salas")
    private List<SalasHasProgramas> salasHasProgramasList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idsala")
    private List<Reservas> reservasList;
    
    @Transient
    private ScheduleModel reservasSchedule;

    public Salas() {
    }

    public Salas(Integer idsala) {
        this.idsala = idsala;
    }

    public Salas(Integer idsala, String nombre, int capMin, int capMax, boolean activo) {
        this.idsala = idsala;
        this.nombre = nombre;
        this.capMin = capMin;
        this.capMax = capMax;
        this.activo = activo;
    }

    public Integer getIdsala() {
        return idsala;
    }

    public void setIdsala(Integer idsala) {
        this.idsala = idsala;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getNumEquipos() {
        return numEquipos;
    }

    public void setNumEquipos(Integer numEquipos) {
        this.numEquipos = numEquipos;
    }

    public int getCapMin() {
        return capMin;
    }

    public void setCapMin(int capMin) {
        this.capMin = capMin;
    }

    public int getCapMax() {
        return capMax;
    }

    public void setCapMax(int capMax) {
        this.capMax = capMax;
    }

    public boolean getActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    @XmlTransient
    public List<SalasHasProgramas> getSalasHasProgramasList() {
        return salasHasProgramasList;
    }

    public void setSalasHasProgramasList(List<SalasHasProgramas> salasHasProgramasList) {
        this.salasHasProgramasList = salasHasProgramasList;
    }

    @XmlTransient
    public List<Reservas> getReservasList() {
        return reservasList;
    }

    public void setReservasList(List<Reservas> reservasList) {
        this.reservasList = reservasList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idsala != null ? idsala.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Salas)) {
            return false;
        }
        Salas other = (Salas) object;
        if ((this.idsala == null && other.idsala != null) || (this.idsala != null && !this.idsala.equals(other.idsala))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.andco.salasucc.model.Salas[ idsala=" + idsala + " ]";
    }

    public ScheduleModel getReservasSchedule() {
        return reservasSchedule;
    }

    public void setReservasSchedule() {
        this.reservasSchedule = new DefaultScheduleModel();
        for (int i = 0; i < reservasList.size(); i++) {
            Reservas reserva = reservasList.get(i);
            if (reserva.getActivo()) {
                this.reservasSchedule.addEvent(new DefaultScheduleEvent(reserva.getAsignatura() + "\n" + reserva.getIdusuario().getNombre() + " " + reserva.getIdusuario().getApellido()  + "\n" + reserva.getGrupo() + "\n" + reserva.getIdusuario().getCorreo(), reserva.getFechaInicio(), reserva.getFechaFin()));
            }
        }
        
    }

 
    
}