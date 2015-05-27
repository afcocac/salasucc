/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.andco.salasucc.model;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author ANDCO
 */
@Entity
@Table(name = "salas_has_programas")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SalasHasProgramas.findAll", query = "SELECT s FROM SalasHasProgramas s"),
    @NamedQuery(name = "SalasHasProgramas.findByIdsala", query = "SELECT s FROM SalasHasProgramas s WHERE s.salasHasProgramasPK.idsala = :idsala"),
    @NamedQuery(name = "SalasHasProgramas.findByIdprograma", query = "SELECT s FROM SalasHasProgramas s WHERE s.salasHasProgramasPK.idprograma = :idprograma"),
    @NamedQuery(name = "SalasHasProgramas.findByActivo", query = "SELECT s FROM SalasHasProgramas s WHERE s.activo = :activo")})
public class SalasHasProgramas implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected SalasHasProgramasPK salasHasProgramasPK;
    @Basic(optional = false)
    @NotNull
    @Column(name = "activo")
    private boolean activo;
    @JoinColumn(name = "idprograma", referencedColumnName = "idprograma", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Programas programas;
    @JoinColumn(name = "idsala", referencedColumnName = "idsala", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Salas salas;

    public SalasHasProgramas() {
    }

    public SalasHasProgramas(SalasHasProgramasPK salasHasProgramasPK) {
        this.salasHasProgramasPK = salasHasProgramasPK;
    }

    public SalasHasProgramas(SalasHasProgramasPK salasHasProgramasPK, boolean activo) {
        this.salasHasProgramasPK = salasHasProgramasPK;
        this.activo = activo;
    }

    public SalasHasProgramas(int idsala, int idprograma) {
        this.salasHasProgramasPK = new SalasHasProgramasPK(idsala, idprograma);
    }

    public SalasHasProgramasPK getSalasHasProgramasPK() {
        return salasHasProgramasPK;
    }

    public void setSalasHasProgramasPK(SalasHasProgramasPK salasHasProgramasPK) {
        this.salasHasProgramasPK = salasHasProgramasPK;
    }

    public boolean getActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public Programas getProgramas() {
        return programas;
    }

    public void setProgramas(Programas programas) {
        this.programas = programas;
    }

    public Salas getSalas() {
        return salas;
    }

    public void setSalas(Salas salas) {
        this.salas = salas;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (salasHasProgramasPK != null ? salasHasProgramasPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SalasHasProgramas)) {
            return false;
        }
        SalasHasProgramas other = (SalasHasProgramas) object;
        if ((this.salasHasProgramasPK == null && other.salasHasProgramasPK != null) || (this.salasHasProgramasPK != null && !this.salasHasProgramasPK.equals(other.salasHasProgramasPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.andco.salasucc.model.SalasHasProgramas[ salasHasProgramasPK=" + salasHasProgramasPK + " ]";
    }
    
}
