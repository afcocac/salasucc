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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author ANDCO
 */
@Entity
@Table(name = "programas")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Programas.findAll", query = "SELECT p FROM Programas p"),
    @NamedQuery(name = "Programas.findByIdprograma", query = "SELECT p FROM Programas p WHERE p.idprograma = :idprograma"),
    @NamedQuery(name = "Programas.findByNombre", query = "SELECT p FROM Programas p WHERE p.nombre = :nombre"),
    @NamedQuery(name = "Programas.findByActivo", query = "SELECT p FROM Programas p WHERE p.activo = :activo")})
public class Programas implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idprograma")
    private Integer idprograma;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "nombre")
    private String nombre;
    @Basic(optional = false)
    @NotNull
    @Column(name = "activo")
    private boolean activo;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "programas")
    private List<SalasHasProgramas> salasHasProgramasList;
    @JoinColumn(name = "idgrupo_programas", referencedColumnName = "idgrupo_programas")
    @ManyToOne(optional = false)
    private GruposProgramas idgrupoProgramas;

    public Programas() {
    }

    public Programas(Integer idprograma) {
        this.idprograma = idprograma;
    }

    public Programas(Integer idprograma, String nombre, boolean activo) {
        this.idprograma = idprograma;
        this.nombre = nombre;
        this.activo = activo;
    }

    public Integer getIdprograma() {
        return idprograma;
    }

    public void setIdprograma(Integer idprograma) {
        this.idprograma = idprograma;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
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

    public GruposProgramas getIdgrupoProgramas() {
        return idgrupoProgramas;
    }

    public void setIdgrupoProgramas(GruposProgramas idgrupoProgramas) {
        this.idgrupoProgramas = idgrupoProgramas;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idprograma != null ? idprograma.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Programas)) {
            return false;
        }
        Programas other = (Programas) object;
        if ((this.idprograma == null && other.idprograma != null) || (this.idprograma != null && !this.idprograma.equals(other.idprograma))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.andco.salasucc.model.Programas[ idprograma=" + idprograma + " ]";
    }
    
}
