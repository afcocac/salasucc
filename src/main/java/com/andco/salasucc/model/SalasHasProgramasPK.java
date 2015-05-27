/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.andco.salasucc.model;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

/**
 *
 * @author ANDCO
 */
@Embeddable
public class SalasHasProgramasPK implements Serializable {
    @Basic(optional = false)
    @NotNull
    @Column(name = "idsala")
    private int idsala;
    @Basic(optional = false)
    @NotNull
    @Column(name = "idprograma")
    private int idprograma;

    public SalasHasProgramasPK() {
    }

    public SalasHasProgramasPK(int idsala, int idprograma) {
        this.idsala = idsala;
        this.idprograma = idprograma;
    }

    public int getIdsala() {
        return idsala;
    }

    public void setIdsala(int idsala) {
        this.idsala = idsala;
    }

    public int getIdprograma() {
        return idprograma;
    }

    public void setIdprograma(int idprograma) {
        this.idprograma = idprograma;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) idsala;
        hash += (int) idprograma;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SalasHasProgramasPK)) {
            return false;
        }
        SalasHasProgramasPK other = (SalasHasProgramasPK) object;
        if (this.idsala != other.idsala) {
            return false;
        }
        if (this.idprograma != other.idprograma) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.andco.salasucc.model.SalasHasProgramasPK[ idsala=" + idsala + ", idprograma=" + idprograma + " ]";
    }
    
}
