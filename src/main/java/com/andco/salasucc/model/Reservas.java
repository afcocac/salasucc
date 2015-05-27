/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.andco.salasucc.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author ANDCO
 */
@Entity
@Table(name = "reservas")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Reservas.findAll", query = "SELECT r FROM Reservas r"),
    @NamedQuery(name = "Reservas.findByIdreserva", query = "SELECT r FROM Reservas r WHERE r.idreserva = :idreserva"),
    @NamedQuery(name = "Reservas.findByNumEstudiantes", query = "SELECT r FROM Reservas r WHERE r.numEstudiantes = :numEstudiantes"),
    @NamedQuery(name = "Reservas.findByPrograma", query = "SELECT r FROM Reservas r WHERE r.programa = :programa"),
    @NamedQuery(name = "Reservas.findByFechaInicio", query = "SELECT r FROM Reservas r WHERE r.fechaInicio = :fechaInicio"),
    @NamedQuery(name = "Reservas.findByFechaFin", query = "SELECT r FROM Reservas r WHERE r.fechaFin = :fechaFin"),
    @NamedQuery(name = "Reservas.findByAsignatura", query = "SELECT r FROM Reservas r WHERE r.asignatura = :asignatura"),
    @NamedQuery(name = "Reservas.findByGrupo", query = "SELECT r FROM Reservas r WHERE r.grupo = :grupo"),
    @NamedQuery(name = "Reservas.findByFranja", query = "SELECT r FROM Reservas r WHERE r.franja = :franja"),
    @NamedQuery(name = "Reservas.findByFechaCreacion", query = "SELECT r FROM Reservas r WHERE r.fechaCreacion = :fechaCreacion"),
    @NamedQuery(name = "Reservas.findByActivo", query = "SELECT r FROM Reservas r WHERE r.activo = :activo")})
public class Reservas implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idreserva")
    private Integer idreserva;
    @Basic(optional = false)
    @NotNull
    @Column(name = "num_estudiantes")
    private int numEstudiantes;
    @Size(max = 45)
    @Column(name = "programa")
    private String programa;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_inicio")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaInicio;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_fin")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaFin;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "asignatura")
    private String asignatura;
    @Basic(optional = false)
    @NotNull
    @Column(name = "grupo")
    private int grupo;
    @Basic(optional = false)
    @NotNull
    @Column(name = "franja")
    private boolean franja;
    @Lob
    @Column(name = "guia")
    private byte[] guia;
    @Column(name = "fecha_creacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCreacion;
    @Basic(optional = false)
    @NotNull
    @Column(name = "activo")
    private boolean activo;
    @JoinColumn(name = "idmaterial_adicional", referencedColumnName = "idmaterial_adicional")
    @ManyToOne
    private MaterialesAdicionales idmaterialAdicional;
    @JoinColumn(name = "idsala", referencedColumnName = "idsala")
    @ManyToOne(optional = false)
    private Salas idsala;
    @JoinColumn(name = "idusuario", referencedColumnName = "idusuario")
    @ManyToOne(optional = false)
    private Usuarios idusuario;

    public Reservas() {
    }

    public Reservas(Integer idreserva) {
        this.idreserva = idreserva;
    }

    public Reservas(Integer idreserva, int numEstudiantes, Date fechaInicio, Date fechaFin, String asignatura, int grupo, boolean franja, boolean activo) {
        this.idreserva = idreserva;
        this.numEstudiantes = numEstudiantes;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.asignatura = asignatura;
        this.grupo = grupo;
        this.franja = franja;
        this.activo = activo;
    }

    public Integer getIdreserva() {
        return idreserva;
    }

    public void setIdreserva(Integer idreserva) {
        this.idreserva = idreserva;
    }

    public int getNumEstudiantes() {
        return numEstudiantes;
    }

    public void setNumEstudiantes(int numEstudiantes) {
        this.numEstudiantes = numEstudiantes;
    }

    public String getPrograma() {
        return programa;
    }

    public void setPrograma(String programa) {
        this.programa = programa;
    }

    public Date getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Date getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(Date fechaFin) {
        this.fechaFin = fechaFin;
    }

    public String getAsignatura() {
        return asignatura;
    }

    public void setAsignatura(String asignatura) {
        this.asignatura = asignatura;
    }

    public int getGrupo() {
        return grupo;
    }

    public void setGrupo(int grupo) {
        this.grupo = grupo;
    }

    public boolean getFranja() {
        return franja;
    }

    public void setFranja(boolean franja) {
        this.franja = franja;
    }

    public byte[] getGuia() {
        return guia;
    }

    public void setGuia(byte[] guia) {
        this.guia = guia;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public boolean getActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public MaterialesAdicionales getIdmaterialAdicional() {
        return idmaterialAdicional;
    }

    public void setIdmaterialAdicional(MaterialesAdicionales idmaterialAdicional) {
        this.idmaterialAdicional = idmaterialAdicional;
    }

    public Salas getIdsala() {
        return idsala;
    }

    public void setIdsala(Salas idsala) {
        this.idsala = idsala;
    }

    public Usuarios getIdusuario() {
        return idusuario;
    }

    public void setIdusuario(Usuarios idusuario) {
        this.idusuario = idusuario;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idreserva != null ? idreserva.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Reservas)) {
            return false;
        }
        Reservas other = (Reservas) object;
        if ((this.idreserva == null && other.idreserva != null) || (this.idreserva != null && !this.idreserva.equals(other.idreserva))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.andco.salasucc.model.Reservas[ idreserva=" + idreserva + " ]";
    }
    
}
