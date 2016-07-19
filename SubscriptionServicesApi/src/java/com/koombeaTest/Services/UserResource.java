/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.koombeaTest.Services;

import com.koombeaTest.Entities.Usuario;
import com.koombeaTest.GenericObjects.GenericJSONResponse;
import com.koombeaTest.GenericObjects.MD5Utility;
import com.koombeaTest.GenericObjects.SessionManager;
import com.koombeaTest.facade.UsuariosFacade;
import javax.ejb.EJB;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.core.MediaType;

/**
 * REST Web Service
 *
 * @author fernando
 */
@Path("user")
public class UserResource {

    @Context
    private UriInfo context;
    @EJB
    private UsuariosFacade usuarioFacade;
    /**
     * Creates a new instance of UserResource
     */
    public UserResource() {
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("login")
    public String login(@Context HttpServletRequest httpServletRequest, @FormParam("username") String username, @FormParam("password") String password) {
        MD5Utility md5=new MD5Utility();
        Usuario u=usuarioFacade.login(username, md5.MD5Hash(password));
        if (u!=null) {
            SessionManager sessionManager = new SessionManager(httpServletRequest,true);
            System.out.println(u);
            
            System.out.println("session Data"+sessionManager);
            sessionManager.getSession().setAttribute("userID", u);
            GenericJSONResponse response = new GenericJSONResponse(true, GenericJSONResponse.SUCCESFUL_PROCESSED_STATUS, "Inicio de sesiÃ³n con exito");
            return response.toString();
        } else {
            GenericJSONResponse response = new GenericJSONResponse(true, GenericJSONResponse.INVALID_SESSION_STATUS, "Usuario o contraseña invalido");
            return response.toString();
        }
    }

    @POST
    @Path("logout")
    @Produces("application/json")
    public String logout(@Context HttpServletRequest request) {
        String response;
        SessionManager sessionManager = new SessionManager(request);
        boolean res = sessionManager.closeSession();
        if (res == true) {
            response = new GenericJSONResponse(true, GenericJSONResponse.SUCCESFUL_PROCESSED_STATUS, "sesiÃ³n terminada correctamente").toString();
        } else {
            response = new GenericJSONResponse(false, GenericJSONResponse.CONFLICT_STATUS, "No existe sesiÃ³n a terminar").toString();
        }
        return response;
    }
}
