/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.andco.salasucc.bean;

import com.andco.salasucc.controller.Encrypt;
import com.andco.salasucc.controller.UsuariosJpaController;
import com.andco.salasucc.model.Usuarios;
import com.andco.session.Util;
import java.security.NoSuchAlgorithmException;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 *
 * @author ANDCO
 */
@ManagedBean(name="loginBean")
@SessionScoped
public class loginBean {
    
    
    private String usuario;
    private String password;
    
    /**
     * Creates a new instance of RegistroBean
     */
    public loginBean() {
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    public String login() throws NoSuchAlgorithmException {
        
        UsuariosJpaController usuariosCont = new UsuariosJpaController();
        Usuarios user = usuariosCont.findUsuarioByCorreo(usuario);
        FacesContext context = FacesContext.getCurrentInstance();
        
        if (user != null) {
            if (user.getPassword().equals(Encrypt.hash256(password))) {
                HttpSession session = Util.getSession();
                session.setAttribute("username", user);
                // es laborista
                if (user.getPerfil()) {
                    return "buscar";
                } else {
                    return "lista";
                }
            }
        }

        FacesMessage errorMessage = new FacesMessage("Usuario y/o contraseña inválida");
        errorMessage.setSeverity(FacesMessage.SEVERITY_ERROR);
        context.addMessage(null, errorMessage);
        
        return null;
    }

}