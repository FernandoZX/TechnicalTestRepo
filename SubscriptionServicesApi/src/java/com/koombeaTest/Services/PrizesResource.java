/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.koombeaTest.Services;

import com.koombeaTest.Entities.Prizes;
import com.koombeaTest.GenericObjects.GenericJSONResponse;
import com.koombeaTest.GenericObjects.SessionManager;
import com.koombeaTest.facade.PrizesFacade;
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
@Path("prizes")
public class PrizesResource {

    @Context
    private UriInfo context;
    @EJB
    private PrizesFacade prizesFacade;

    /**
     * Creates a new instance of PrizesResource
     */
    public PrizesResource() {
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("crear")
    public String create(@Context HttpServletRequest httpServletRequest, @FormParam("prizesName") String nombre) {
        String response = "";
        SessionManager sessionManager = new SessionManager(httpServletRequest);
        if (sessionManager.validateSession()) {
            if (nombre.isEmpty()) {
                return new GenericJSONResponse(false, GenericJSONResponse.CONFLICT_STATUS, "Prize name required").toString();
            }
            response = prizesFacade.createPrize(nombre);
            if (response.equalsIgnoreCase("SUCCESS")) {
                response = new GenericJSONResponse(true, GenericJSONResponse.SUCCESFUL_PROCESSED_STATUS, "Prize created successful").toString();
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
    public String edit(@Context HttpServletRequest httpServletRequest, @FormParam("idPrizes") Integer prizeID, @FormParam("prizesName") String nombre) {
        String response = "";
        SessionManager sessionManager = new SessionManager(httpServletRequest);
        if (sessionManager.validateSession()) {
            if (prizeID == null) {
                return new GenericJSONResponse(false, GenericJSONResponse.CONFLICT_STATUS, "Prize ID required").toString();
            }
            if (nombre.isEmpty()) {
                return new GenericJSONResponse(false, GenericJSONResponse.CONFLICT_STATUS, "Prize name required").toString();
            }
            Prizes prize = prizesFacade.findPrizes(prizeID);
            if (prize == null) {
                return new GenericJSONResponse(false, GenericJSONResponse.CONFLICT_STATUS, "Prize no found by this ID: " + prizeID).toString();
            }
            response = prizesFacade.editPrizes(prize, nombre);
            if (response.equalsIgnoreCase("SUCCESS")) {
                response = new GenericJSONResponse(true, GenericJSONResponse.SUCCESFUL_PROCESSED_STATUS, "Prize edited successful").toString();
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
    @Path("delete")
    public String delete(@Context HttpServletRequest httpServletRequest, @FormParam("idPrizes") Integer prizeID) {
        String response = "";
        SessionManager sessionManager = new SessionManager(httpServletRequest);
        if (sessionManager.validateSession()) {
            if (prizeID == null) {
                return new GenericJSONResponse(false, GenericJSONResponse.CONFLICT_STATUS, "Prize ID required").toString();
            }
            Prizes prize = prizesFacade.findPrizes(prizeID);
            if (prize == null) {
                return new GenericJSONResponse(false, GenericJSONResponse.CONFLICT_STATUS, "Prize no found by this ID: " + prizeID).toString();
            }
            response = prizesFacade.deletePrizes(prize);
            if (response.equalsIgnoreCase("SUCCESS")) {
                response = new GenericJSONResponse(true, GenericJSONResponse.SUCCESFUL_PROCESSED_STATUS, "Prize deleted successful").toString();
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
    public String list(@Context HttpServletRequest httpServletRequest, @FormParam("prizesName") String name, @FormParam("start") int start, @FormParam("limit") int limit) {
        String response = "";
        SessionManager sessionManager = new SessionManager(httpServletRequest);
        if (sessionManager.validateSession()) {
            List<Prizes> list = prizesFacade.listPrizes(name, start, limit);
            Integer total = prizesFacade.countPrizes(name).intValue();
            return new GenericJSONResponse(true, GenericJSONResponse.SUCCESFUL_PROCESSED_STATUS, "Prizes lists", list, total).toString();
        } else {
            response = new GenericJSONResponse(false, GenericJSONResponse.INVALID_SESSION_STATUS, "No exist Session").toString();
        }
        return response;
    }
}
