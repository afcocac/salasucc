/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.andco.salasucc.bean;

import com.andco.salasucc.controller.Encrypt;
import com.andco.salasucc.controller.FacultadesJpaController;
import com.andco.salasucc.controller.UsuariosJpaController;
import com.andco.salasucc.model.Facultades;
import com.andco.salasucc.model.Usuarios;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author ANDCO
 */
@ManagedBean(name="registroBean")
@RequestScoped
public class RegistroBean {
    
    
    private String nombre;
    private String apellido;
    private String correo;
    private String facultad;
    private String password;
    private String passwordConfirm;
    private List<Facultades> listaFacultades;
    
    
    @PostConstruct
    public void init() {
        FacultadesJpaController proCont = new FacultadesJpaController();
        this.listaFacultades = proCont.findFacultadesEntitiesActivas();
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getFacultad() {
        return facultad;
    }

    public void setFacultad(String programa) {
        this.facultad = programa;
    }

    public List<Facultades> getListaFacultades() {
        return listaFacultades;
    }
        
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPasswordConfirm() {
        return passwordConfirm;
    }

    public void setPasswordConfirm(String passwordConfirm) {
        this.passwordConfirm = passwordConfirm;
    }

    public String registrar() throws NoSuchAlgorithmException {

        FacesContext context = FacesContext.getCurrentInstance();
        UsuariosJpaController usuariosController = new UsuariosJpaController();

        if (usuariosController.findUsuarioByCorreo(correo) != null) {
            FacesMessage errorMessage = new FacesMessage("El usuario ya existe");
            errorMessage.setSeverity(FacesMessage.SEVERITY_ERROR);
            context.addMessage(null, errorMessage);
            return null;
        } else {
            if (password.equals(passwordConfirm)) {
                FacultadesJpaController facultadesController = new FacultadesJpaController();

                Usuarios user = new Usuarios();

                user.setCorreo(correo);
                user.setNombre(nombre);
                user.setApellido(apellido);
                user.setPassword(Encrypt.hash256(password));
                user.setPerfil(false);
                user.setActivo(true);
                user.setIdfacultad(facultadesController.findFacultades(Integer.parseInt(facultad)));

                usuariosController.create(user);
                context.addMessage(null, new FacesMessage("Usuario Registrado"));
                return null;
            } else {
                FacesMessage errorMessage = new FacesMessage("Las contraseñas no coinciden");
                errorMessage.setSeverity(FacesMessage.SEVERITY_ERROR);
                context.addMessage(null, errorMessage);
                return null;
            }
        }

    }
}
