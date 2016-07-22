/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.koombeaTest.Services;

import com.koombeaTest.Entities.Store;
import com.koombeaTest.GenericObjects.GenericJSONResponse;
import com.koombeaTest.GenericObjects.SessionManager;
import com.koombeaTest.facade.StoreFacade;
import java.util.List;
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
@Path("store")
public class StoreResource {

    @Context
    private UriInfo context;
    @EJB
    private StoreFacade storeFacade;

    /**
     * Creates a new instance of StoreResource
     */
    public StoreResource() {
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("crear")
    public String create(@Context HttpServletRequest httpServletRequest, @FormParam("nombre") String nombre, @FormParam("descripcion") String descripcion) {
        String response = "";
        SessionManager sessionManager = new SessionManager(httpServletRequest);
        if (sessionManager.validateSession()) {
            if (nombre.isEmpty()) {
                return new GenericJSONResponse(false, GenericJSONResponse.CONFLICT_STATUS, "Store name required").toString();
            }
            if (descripcion.isEmpty()) {
                return new GenericJSONResponse(false, GenericJSONResponse.CONFLICT_STATUS, "Store description required").toString();
            }
            response = storeFacade.createStore(nombre, descripcion);
            if (response.equalsIgnoreCase("SUCCESS")) {
                response = new GenericJSONResponse(true, GenericJSONResponse.SUCCESFUL_PROCESSED_STATUS, "Store created successful").toString();
            } else {
                response = new GenericJSONResponse(false, GenericJSONResponse.INTERNAL_ERROR_STATUS, response).toString();
            }
        } else {
            response = new GenericJSONResponse(false, GenericJSONResponse.INVALID_SESSION_STATUS, "No exist Session").toString();
        }
        return response;
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("edit")
    public String edit(@Context HttpServletRequest httpServletRequest, @FormParam("idTienda") Integer storeID, @FormParam("nombre") String nombre, @FormParam("descripcion") String descripcion) {
        String response = "";
        SessionManager sessionManager = new SessionManager(httpServletRequest);
        if (sessionManager.validateSession()) {
            if (storeID == null) {
                return new GenericJSONResponse(false, GenericJSONResponse.CONFLICT_STATUS, "Store ID required").toString();
            }
            if (nombre.isEmpty()) {
                return new GenericJSONResponse(false, GenericJSONResponse.CONFLICT_STATUS, "Store name required").toString();
            }
            if (descripcion.isEmpty()) {
                return new GenericJSONResponse(false, GenericJSONResponse.CONFLICT_STATUS, "Store description required").toString();
            }
            Store store = storeFacade.findStore(storeID);
            if (store == null) {
                return new GenericJSONResponse(false, GenericJSONResponse.CONFLICT_STATUS, "Store no found by this ID: " + storeID).toString();
            }
            response = storeFacade.editStore(store, nombre, descripcion);
            if (response.equalsIgnoreCase("SUCCESS")) {
                response = new GenericJSONResponse(true, GenericJSONResponse.SUCCESFUL_PROCESSED_STATUS, "Store edited successful").toString();
            } else {
                response = new GenericJSONResponse(false, GenericJSONResponse.INTERNAL_ERROR_STATUS, response).toString();
            }
        } else {
            response = new GenericJSONResponse(false, GenericJSONResponse.INVALID_SESSION_STATUS, "No exist Session").toString();
        }
        return response;
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("list")
    public String list(@Context HttpServletRequest httpServletRequest, @FormParam("nombre") String name, @FormParam("start") int start, @FormParam("limit") int limit) {
        String response = "";
        SessionManager sessionManager = new SessionManager(httpServletRequest);
        if (sessionManager.validateSession()) {
            List<Store> list = storeFacade.listStore(name, start, limit);
            Integer total = storeFacade.countStore(name).intValue();
            return new GenericJSONResponse(true, GenericJSONResponse.SUCCESFUL_PROCESSED_STATUS, "Store lists", list, total).toString();
        } else {
            response = new GenericJSONResponse(false, GenericJSONResponse.INVALID_SESSION_STATUS, "No exist Session").toString();
        }
        return response;
    }
}
