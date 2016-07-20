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
import com.koombeaTest.facade.SubscriptionFacade;
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
@Path("subscription")
public class SubscriptionResource {

    @Context
    private UriInfo context;
    @EJB
    private SubscriptionFacade subscriptionFacade;

    @EJB
    private StoreFacade storeFacade;

    /**
     * Creates a new instance of SubscriptionResource
     */
    public SubscriptionResource() {
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("subscribe")
    public String create(@Context HttpServletRequest httpServletRequest, @FormParam("email") String email, @FormParam("storeID") Integer storeID) {
        String response = "";

        if (email.isEmpty()) {
            return new GenericJSONResponse(false, GenericJSONResponse.CONFLICT_STATUS, "email field required").toString();
        }
        if (storeID == null) {
            return new GenericJSONResponse(false, GenericJSONResponse.CONFLICT_STATUS, "Store ID required").toString();
        }
        Store store = storeFacade.findStore(storeID);
        if (store == null) {
            return new GenericJSONResponse(false, GenericJSONResponse.CONFLICT_STATUS, "Store no found by this ID: " + storeID).toString();
        }
        java.util.Date date = new java.util.Date();
        boolean validateSubscription = subscriptionFacade.validateDateSubs(email, store, date);
        if (validateSubscription) {
            return new GenericJSONResponse(false, GenericJSONResponse.CONFLICT_STATUS, "Cant subscribe twice on this day").toString();
        }
        response = subscriptionFacade.createSubscription(email, store, date);
        if (response.equalsIgnoreCase("SUCCESS")) {
            response = new GenericJSONResponse(true, GenericJSONResponse.SUCCESFUL_PROCESSED_STATUS, "Subscription successful").toString();
        } else {
            response = new GenericJSONResponse(false, GenericJSONResponse.INTERNAL_ERROR_STATUS, response).toString();
        }

        return response;
    }

}
