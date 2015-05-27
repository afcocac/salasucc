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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author ANDCO
 */
@Entity
@Table(name = "grupos_programas")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "GruposProgramas.findAll", query = "SELECT g FROM GruposProgramas g"),
    @NamedQuery(name = "GruposProgramas.findByIdgrupoProgramas", query = "SELECT g FROM GruposProgramas g WHERE g.idgrupoProgramas = :idgrupoProgramas"),
    @NamedQuery(name = "GruposProgramas.findByNombre", query = "SELECT g FROM GruposProgramas g WHERE g.nombre = :nombre"),
    @NamedQuery(name = "GruposProgramas.findByActivo", query = "SELECT g FROM GruposProgramas g WHERE g.activo = :activo")})
public class GruposProgramas implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idgrupo_programas")
    private Integer idgrupoProgramas;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "nombre")
    private String nombre;
    @Basic(optional = false)
    @NotNull
    @Column(name = "activo")
    private boolean activo;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idgrupoProgramas")
    private List<Programas> programasList;

    public GruposProgramas() {
    }

    public GruposProgramas(Integer idgrupoProgramas) {
        this.idgrupoProgramas = idgrupoProgramas;
    }

    public GruposProgramas(Integer idgrupoProgramas, String nombre, boolean activo) {
        this.idgrupoProgramas = idgrupoProgramas;
        this.nombre = nombre;
        this.activo = activo;
    }

    public Integer getIdgrupoProgramas() {
        return idgrupoProgramas;
    }

    public void setIdgrupoProgramas(Integer idgrupoProgramas) {
        this.idgrupoProgramas = idgrupoProgramas;
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
    public List<Programas> getProgramasList() {
        return programasList;
    }

    public void setProgramasList(List<Programas> programasList) {
        this.programasList = programasList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idgrupoProgramas != null ? idgrupoProgramas.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GruposProgramas)) {
            return false;
        }
        GruposProgramas other = (GruposProgramas) object;
        if ((this.idgrupoProgramas == null && other.idgrupoProgramas != null) || (this.idgrupoProgramas != null && !this.idgrupoProgramas.equals(other.idgrupoProgramas))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.andco.salasucc.model.GruposProgramas[ idgrupoProgramas=" + idgrupoProgramas + " ]";
    }
    
}
