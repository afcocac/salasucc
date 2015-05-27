/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.andco.salasucc.model;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
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
@Table(name = "materiales_adicionales")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "MaterialesAdicionales.findAll", query = "SELECT m FROM MaterialesAdicionales m"),
    @NamedQuery(name = "MaterialesAdicionales.findByIdmaterialAdicional", query = "SELECT m FROM MaterialesAdicionales m WHERE m.idmaterialAdicional = :idmaterialAdicional"),
    @NamedQuery(name = "MaterialesAdicionales.findByNombre", query = "SELECT m FROM MaterialesAdicionales m WHERE m.nombre = :nombre"),
    @NamedQuery(name = "MaterialesAdicionales.findByActivo", query = "SELECT m FROM MaterialesAdicionales m WHERE m.activo = :activo")})
public class MaterialesAdicionales implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idmaterial_adicional")
    private Integer idmaterialAdicional;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "nombre")
    private String nombre;
    @Basic(optional = false)
    @NotNull
    @Column(name = "activo")
    private boolean activo;
    @OneToMany(mappedBy = "idmaterialAdicional")
    private List<Reservas> reservasList;

    public MaterialesAdicionales() {
    }

    public MaterialesAdicionales(Integer idmaterialAdicional) {
        this.idmaterialAdicional = idmaterialAdicional;
    }

    public MaterialesAdicionales(Integer idmaterialAdicional, String nombre, boolean activo) {
        this.idmaterialAdicional = idmaterialAdicional;
        this.nombre = nombre;
        this.activo = activo;
    }

    public Integer getIdmaterialAdicional() {
        return idmaterialAdicional;
    }

    public void setIdmaterialAdicional(Integer idmaterialAdicional) {
        this.idmaterialAdicional = idmaterialAdicional;
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
    public List<Reservas> getReservasList() {
        return reservasList;
    }

    public void setReservasList(List<Reservas> reservasList) {
        this.reservasList = reservasList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idmaterialAdicional != null ? idmaterialAdicional.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MaterialesAdicionales)) {
            return false;
        }
        MaterialesAdicionales other = (MaterialesAdicionales) object;
        if ((this.idmaterialAdicional == null && other.idmaterialAdicional != null) || (this.idmaterialAdicional != null && !this.idmaterialAdicional.equals(other.idmaterialAdicional))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.andco.salasucc.model.MaterialesAdicionales[ idmaterialAdicional=" + idmaterialAdicional + " ]";
    }
    
}
