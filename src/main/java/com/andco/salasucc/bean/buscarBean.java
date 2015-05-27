/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.andco.salasucc.bean;

import com.andco.salasucc.controller.SalasJpaController;
import com.andco.salasucc.model.Salas;
import com.andco.session.Util;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

/**
 *
 * @author ANDCO
 */
@ManagedBean(name="buscarBean")
@RequestScoped
public class buscarBean {
    
    private List<Salas> listaSalas;
    
    @PostConstruct
    public void init() {
        SalasJpaController salasCont = new SalasJpaController();
        this.listaSalas = salasCont.findSalasEntitiesActivas();
        for (int i = 0; i < listaSalas.size(); i++) {
            listaSalas.get(i).setReservasSchedule();
        }
    }

    public List<Salas> getListaSalas() {
        return listaSalas;
    }
    
    public String cerrarSesion() {
        HttpSession session = Util.getSession();
        session.invalidate();
        return "index";
    }
    
}
