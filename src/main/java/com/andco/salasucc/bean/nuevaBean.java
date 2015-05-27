/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.andco.salasucc.bean;

import com.andco.salasucc.controller.GruposProgramasJpaController;
import com.andco.salasucc.controller.MaterialesAdicionalesJpaController;
import com.andco.salasucc.controller.ProgramasJpaController;
import com.andco.salasucc.controller.ReservasJpaController;
import com.andco.salasucc.controller.SalasJpaController;
import com.andco.salasucc.controller.UsuariosJpaController;
import com.andco.salasucc.model.GruposProgramas;
import com.andco.salasucc.model.MaterialesAdicionales;
import com.andco.salasucc.model.Programas;
import com.andco.salasucc.model.Reservas;
import com.andco.salasucc.model.Salas;
import com.andco.salasucc.model.Usuarios;
import com.andco.session.Util;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.faces.model.SelectItemGroup;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
import org.apache.commons.io.IOUtils;

/**
 *
 * @author ANDCO
 */
@ManagedBean(name = "nuevaBean")
@SessionScoped
public class nuevaBean implements Serializable {

    private String numEstudiantes;
    private String software;
    private Date fecha;
    private boolean recurrente;
    private String sala;
    private Date horaInicio;
    private Date horaFin;
    private String materialAdicional;
    private String asignatura;
    private String grupo;
    private String franja;
    private Part guia;
    private List<SelectItem> gruposPrograma;
    private List<Salas> listaSalasDisponibles;
    private List<MaterialesAdicionales> listaMaterialesAdicionales;
    private Date hoy;
    List<Date> listaFechasInicio;
    List<Date> listaFechasFin;

    @PostConstruct
    public void init() {
        hoy = new Date();

        software = "1";
        sala = "1";
        GruposProgramasJpaController proCont = new GruposProgramasJpaController();

        List<GruposProgramas> listaGruposProgramas = proCont.findGruposProgramasEntitiesActivas();

        gruposPrograma = new ArrayList<>();
        List<SelectItemGroup> listaItemGrupo = new ArrayList<>();
        for (int i = 0; i < listaGruposProgramas.size(); i++) {
            listaItemGrupo.add(new SelectItemGroup(listaGruposProgramas.get(i).getNombre()));
        }

        List<SelectItem[]> programas = new ArrayList<>();
        for (int i = 0; i < listaGruposProgramas.size(); i++) {
            List<Programas> lPro = listaGruposProgramas.get(i).getProgramasList();
            List<Programas> lProActivos = new ArrayList<>();
            for (int j = 0; j < lPro.size(); j++) {
                if (lPro.get(j).getActivo()) {
                    lProActivos.add(lPro.get(j));
                }
            }
            SelectItem[] lIte = new SelectItem[lProActivos.size()];
            for (int j = 0; j < lProActivos.size(); j++) {
                    lIte[j] = new SelectItem(lProActivos.get(j).getIdprograma(), lProActivos.get(j).getNombre());
            }
            programas.add(lIte);
        }

        for (int i = 0; i < listaItemGrupo.size(); i++) {
            listaItemGrupo.get(i).setSelectItems(programas.get(i));
        }

        for (int i = 0; i < listaItemGrupo.size(); i++) {
            gruposPrograma.add(listaItemGrupo.get(i));
        }

    }

    public String getNumEstudiantes() {
        return numEstudiantes;
    }

    public void setNumEstudiantes(String numEstudiantes) {
        this.numEstudiantes = numEstudiantes;
    }

    public String getSoftware() {
        ProgramasJpaController proCont = new ProgramasJpaController();
        return proCont.findProgramas(Integer.parseInt(software)).getNombre();
    }

    public void setSoftware(String software) {
        this.software = software;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public boolean isRecurrente() {
        return recurrente;
    }

    public void setRecurrente(boolean recurrente) {
        this.recurrente = recurrente;
    }

    public String getSala() {
        SalasJpaController salaCont = new SalasJpaController();
        return salaCont.findSalas(Integer.parseInt(sala)).getNombre();
    }

    public void setSala(String sala) {
        this.sala = sala;
    }

    public Date getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(Date horaInicio) {
        this.horaInicio = horaInicio;
    }

    public Date getHoraFin() {
        return horaFin;
    }

    public void setHoraFin(Date horaFin) {
        this.horaFin = horaFin;
    }

    public String getMaterialAdicional() {
        return materialAdicional;
    }

    public void setMaterialAdicional(String materialAdicional) {
        this.materialAdicional = materialAdicional;
    }

    public String getAsignatura() {
        return asignatura;
    }

    public void setAsignatura(String asignatura) {
        this.asignatura = asignatura;
    }

    public String getGrupo() {
        return grupo;
    }

    public void setGrupo(String grupo) {
        this.grupo = grupo;
    }

    public String getFranja() {
        return franja;
    }

    public void setFranja(String franja) {
        this.franja = franja;
    }

    public Part getGuia() {
        return guia;
    }

    public void setGuia(Part guia) {
        this.guia = guia;
    }

    public List<SelectItem> getGruposPrograma() {
        return gruposPrograma;
    }

    public List<Salas> getListaSalasDisponibles() {
        return listaSalasDisponibles;
    }

    public List<MaterialesAdicionales> getListaMaterialesAdicionales() {
        return listaMaterialesAdicionales;
    }

    public Date getHoy() {
        return hoy;
    }

    public String siguiente() {
        FacesContext context = FacesContext.getCurrentInstance();

        horaInicio.setYear(fecha.getYear());
        horaInicio.setMonth(fecha.getMonth());
        horaInicio.setDate(fecha.getDate());
        horaFin.setYear(fecha.getYear());
        horaFin.setMonth(fecha.getMonth());
        horaFin.setDate(fecha.getDate());
        if ((horaFin.getTime() - horaInicio.getTime()) < 3600000) {
            FacesMessage errorMessage = new FacesMessage("Fecha Invalida");
            errorMessage.setSeverity(FacesMessage.SEVERITY_ERROR);
            context.addMessage(null, errorMessage);
            horaFin = null;
            return null;
        } else {
            SalasJpaController salasCont = new SalasJpaController();
            listaSalasDisponibles = new ArrayList<>();
            if (!recurrente) {
                listaSalasDisponibles = salasCont.buscarSalasDisponibles(Integer.parseInt(numEstudiantes), Integer.parseInt(software), horaInicio, horaFin);
            } else {
                Date fechaFinSemestre = new Date();
                fechaFinSemestre.setHours(23);
                fechaFinSemestre.setMinutes(59);

                if (fecha.getMonth() >= 1 && fecha.getMonth() <= 4) {
                    fechaFinSemestre.setMonth(4);
                    fechaFinSemestre.setDate(31);
                } else {
                    fechaFinSemestre.setMonth(10);
                    fechaFinSemestre.setDate(30);
                }
                listaFechasInicio = generaFechasRecurrentes(horaInicio, fechaFinSemestre);
                listaFechasFin = generaFechasRecurrentes(horaFin, fechaFinSemestre);
                HashMap<Salas, Integer> map = new HashMap<>();

                int tam = listaFechasInicio.size();
                for (int i = 0; i < tam; i++) {
                    List<Salas> lSalas = salasCont.buscarSalasDisponibles(Integer.parseInt(numEstudiantes), Integer.parseInt(software), listaFechasInicio.get(i), listaFechasFin.get(i));
                    for (int j = 0; j < lSalas.size(); j++) {
                        Salas salaActual = lSalas.get(j);
                        Integer previousValue = map.get(salaActual);
                        map.put(salaActual, previousValue == null ? 1 : previousValue + 1);
                    }
                }

                for (Salas key : map.keySet()) {
                    if (map.get(key) == tam) {
                        listaSalasDisponibles.add(key);
                    }
                }

            }

            if (listaSalasDisponibles.isEmpty()) {
                FacesMessage warnMessage = new FacesMessage("No hay salas disponibles");
                warnMessage.setSeverity(FacesMessage.SEVERITY_WARN);
                context.addMessage(null, warnMessage);
                return null;
            } else {
                return "disponibles";
            }

        }
    }

    public String verDisponibles() {
        MaterialesAdicionalesJpaController materialesCont = new MaterialesAdicionalesJpaController();
        listaMaterialesAdicionales = materialesCont.findMaterialesAdicionalesEntitiesActivas();
        return "horario";
    }

    public String reservar() throws IOException {

        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        HttpSession httpSession = request.getSession(false);
        Usuarios usuario = (Usuarios) httpSession.getAttribute("username");

        UsuariosJpaController usuariosController = new UsuariosJpaController();
        SalasJpaController salasController = new SalasJpaController();
        MaterialesAdicionalesJpaController materialesCont = new MaterialesAdicionalesJpaController();
        ReservasJpaController reservasController = new ReservasJpaController();
        
        

        Reservas reserva = new Reservas();
        reserva.setNumEstudiantes(Integer.parseInt(numEstudiantes));
        reserva.setPrograma(getSoftware());
        reserva.setAsignatura(asignatura);
        reserva.setGrupo(Integer.parseInt(grupo));
        reserva.setFranja(Boolean.parseBoolean(franja));
        reserva.setFechaCreacion(new Date());
        reserva.setActivo(true);
        reserva.setIdusuario(usuario);
        reserva.setIdsala(salasController.findSalas(Integer.parseInt(sala)));
        reserva.setIdmaterialAdicional(materialesCont.findMaterialesAdicionales(Integer.parseInt(materialAdicional)));
        
        //archivo
        if (guia != null) {
            InputStream is = guia.getInputStream();
            byte[] guiaByte = IOUtils.toByteArray(is);
            reserva.setGuia(guiaByte);
        }

        if (!recurrente) {
            reserva.setFechaInicio(horaInicio);
            reserva.setFechaFin(horaFin);
            reservasController.create(reserva);
        } else {
            for (int i = 0; i < listaFechasInicio.size(); i++) {
                reserva.setFechaInicio(listaFechasInicio.get(i));
                reserva.setFechaFin(listaFechasFin.get(i));
                reservasController.create(reserva);
            }
        }

        return "detalle";
    }

    public List<Date> generaFechasRecurrentes(Date fechaInicio, Date fechaFin) {
        List<Date> listaFechas = new ArrayList<>();
        Calendar c = Calendar.getInstance();
        while (fechaInicio.before(fechaFin)) {
            c.setTime(fechaInicio);
            listaFechas.add(c.getTime());
            c.add(Calendar.DATE, 7);
            fechaInicio = c.getTime();
        }
        return listaFechas;
    }

    public String cerrarSesion() {
        HttpSession session = Util.getSession();
        session.invalidate();
        return "index";
    }

}