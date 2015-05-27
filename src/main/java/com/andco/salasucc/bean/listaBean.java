/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.andco.salasucc.bean;

import com.andco.salasucc.controller.ReservasJpaController;
import com.andco.salasucc.controller.UsuariosJpaController;
import com.andco.salasucc.model.Reservas;
import com.andco.salasucc.model.Usuarios;
import com.andco.session.Util;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import net.sf.jmimemagic.Magic;
import net.sf.jmimemagic.MagicException;
import net.sf.jmimemagic.MagicMatch;
import net.sf.jmimemagic.MagicMatchNotFoundException;
import net.sf.jmimemagic.MagicParseException;

/**
 *
 * @author ANDCO
 */
@ManagedBean(name="listaBean")
@ViewScoped
public class listaBean {
    
    private List<Reservas> listaReservas;
    
    @PostConstruct
    public void init() {

        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        HttpSession httpSession = request.getSession(false);
        Usuarios usuario = (Usuarios) httpSession.getAttribute("username");

        listaReservas = usuario.getReservasList();
    }

    public List<Reservas> getListaReservas() {
        return listaReservas;
    }

    public String cerrarSesion() {
        HttpSession session = Util.getSession();
        session.invalidate();
        return "index";
    }

    public void descargarGuia(Reservas reserva) throws IOException, MagicException, MagicParseException, MagicMatchNotFoundException {
        if (reserva.getGuia() != null) {
        // Prepare.

            byte[] pdfData = reserva.getGuia();
            MagicMatch match = Magic.getMagicMatch(pdfData);
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ExternalContext externalContext = facesContext.getExternalContext();
            HttpServletResponse response = (HttpServletResponse) externalContext.getResponse();

            // Initialize response.
            response.reset(); // Some JSF component library or some Filter might have set some headers in the buffer beforehand. We want to get rid of them, else it may collide.
            response.setContentType(match.getMimeType()); // Check http://www.iana.org/assignments/media-types for all types. Use if necessary ServletContext#getMimeType() for auto-detection based on filename.
            response.setHeader("Content-disposition", "attachment; filename=\"guia." + match.getExtension()+ "\""); // The Save As popup magic is done here. You can give it any filename you want, this only won't work in MSIE, it will use current request URL as filename instead.
            try ( // Write file to response.
                    OutputStream output = response.getOutputStream()) {
                output.write(pdfData);
            } catch (Exception e) {
                FacesMessage errorMessage = new FacesMessage("Error");
                errorMessage.setSeverity(FacesMessage.SEVERITY_INFO);
                facesContext.addMessage(null, errorMessage);

            }

            // Inform JSF to not take the response in hands.
            facesContext.responseComplete(); // Important! Else JSF will attempt to render the response which obviously will fail since it's already written with a file and closed.
        }
    }

}
