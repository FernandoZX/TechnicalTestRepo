/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.koombeaTest.Services;

import com.koombeaTest.Entities.Store;
import com.koombeaTest.Entities.Subscription;
import com.koombeaTest.GenericObjects.GenericJSONResponse;
import com.koombeaTest.GenericObjects.SessionManager;
import com.koombeaTest.facade.StoreFacade;
import com.koombeaTest.facade.SubscriptionFacade;
import com.koombeaTest.models.SubscriberModel;
import flexjson.transformer.DateTransformer;
import flexjson.transformer.Transformer;
import java.util.LinkedList;
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

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("list")
    public String list(@Context HttpServletRequest httpServletRequest, @FormParam("storeName") String name, @FormParam("start") int start, @FormParam("limit") int limit) {
        String response = "";
        SessionManager sessionManager = new SessionManager(httpServletRequest);
        if (sessionManager.validateSession()) {
            List<Subscription> list = subscriptionFacade.listSubscriptionByStore(name, start, limit);
            Integer total = subscriptionFacade.countSubscriptionByStore(name).intValue();
            GenericJSONResponse genericJSONResponse = new GenericJSONResponse(true, GenericJSONResponse.SUCCESFUL_PROCESSED_STATUS, "Subscription lists", list, total);
            genericJSONResponse.transform(new DateTransformer("yyyy-MM-dd"), "data.registereddate");
            response = genericJSONResponse.toString();
        } else {
            response = new GenericJSONResponse(false, GenericJSONResponse.INVALID_SESSION_STATUS, "No exist Session").toString();
        }
        return response;
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("listWinners")
    public String listWinners(@Context HttpServletRequest httpServletRequest, @FormParam("storeID") Integer storeID, @FormParam("multiple") int multiple, @FormParam("start") int start, @FormParam("limit") int limit) {
        String response = "";
        SessionManager sessionManager = new SessionManager(httpServletRequest);
        if (sessionManager.validateSession()) {
            Store store = storeFacade.findStore(storeID);
            Integer total=0;
            List<SubscriberModel> subcriberModelList = new LinkedList<SubscriberModel>();
                
            if (store != null) {
                List<Subscription> list = subscriptionFacade.listSubscriptionByStore(store, start, limit);
                total = subscriptionFacade.countSubscriptionByStore(store).intValue();
                if (total > 1000) {
                    if (multiple == 50) {
                        for (Subscription subs : list) {
                            if (subs.getId() % 50 == 0) {
                                SubscriberModel subscriberModel = new SubscriberModel();
                                subscriberModel.setId(subs.getId());
                                subscriberModel.setEmail(subs.getEmail());
                                subscriberModel.setRegistereddate(subs.getRegistereddate());
                                subscriberModel.setStoreid(subs.getIdStore().getId());
                                subscriberModel.setStorename(subs.getIdStore().getNombre());
                                subcriberModelList.add(subscriberModel);
                            }
                        }
                    } else if (multiple == 500) {
                        for (Subscription subs : list) {
                            if (subs.getId() % 500 == 0) {
                                SubscriberModel subscriberModel = new SubscriberModel();
                                subscriberModel.setId(subs.getId());
                                subscriberModel.setEmail(subs.getEmail());
                                subscriberModel.setRegistereddate(subs.getRegistereddate());
                                subscriberModel.setStoreid(subs.getIdStore().getId());
                                subscriberModel.setStorename(subs.getIdStore().getNombre());
                                subcriberModelList.add(subscriberModel);
                            }
                        }
                    }
                } else if (multiple == 500) {
                    for (Subscription subs : list) {
                        if (subs.getId() % 500 == 0) {
                            SubscriberModel subscriberModel = new SubscriberModel();
                            subscriberModel.setId(subs.getId());
                            subscriberModel.setEmail(subs.getEmail());
                            subscriberModel.setRegistereddate(subs.getRegistereddate());
                            subscriberModel.setStoreid(subs.getIdStore().getId());
                            subscriberModel.setStorename(subs.getIdStore().getNombre());
                            subcriberModelList.add(subscriberModel);
                        }
                    }
                }
            }
            GenericJSONResponse genericJSONResponse = new GenericJSONResponse(true, GenericJSONResponse.SUCCESFUL_PROCESSED_STATUS, "Subscription lists", subcriberModelList, total);
            genericJSONResponse.transform(new DateTransformer("yyyy-MM-dd"), "data.registereddate");
            response = genericJSONResponse.toString();
        } else {
            response = new GenericJSONResponse(false, GenericJSONResponse.INVALID_SESSION_STATUS, "No exist Session").toString();
        }
        return response;
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("countSubscribers")
    public String sendWinners(@Context HttpServletRequest httpServletRequest, @FormParam("storeID") int storeID) {
        String response = "";
        SessionManager sessionManager = new SessionManager(httpServletRequest);
        if (sessionManager.validateSession()) {
            Store store = storeFacade.findStore(storeID);
            Long total = subscriptionFacade.countSubscriptionByStore(store);
            return new GenericJSONResponse(true, GenericJSONResponse.SUCCESFUL_PROCESSED_STATUS, "count of subcribers by store", total).toString();
        } else {
            response = new GenericJSONResponse(false, GenericJSONResponse.INVALID_SESSION_STATUS, "No exist Session").toString();
        }
        return response;

    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("sendWinners")
    public String sendWinners(@Context HttpServletRequest httpServletRequest, @FormParam("storeID") int storeID, @FormParam("multiple") Integer multiple) {
        String response = "";
        SessionManager sessionManager = new SessionManager(httpServletRequest);
        if (sessionManager.validateSession()) {
            if (multiple != 50 && multiple != 500) {
                return new GenericJSONResponse(false, GenericJSONResponse.CONFLICT_STATUS, "No valid Multiple").toString();
            }
            int sentCounter = 0;
            Store store=storeFacade.findStore(storeID);
            List<Subscription> list = subscriptionFacade.listSubscriptionByStore(store.getNombre());
            Integer total = subscriptionFacade.countSubscriptionByStore(store.getNombre()).intValue();
            if (total > 1000) {
                if (multiple == 50) {
                    for (Subscription subs : list) {
                        if (subs.getId() % 50 == 0) {
                            String winnermsg = subscriptionFacade.winnerGenerator(subs.getIdStore(), subs.getId());
                            String resultSent = subscriptionFacade.sendEmail(subs.getEmail(), subs.getIdStore(), winnermsg);
                            sentCounter++;
                        }
                    }
                } else if (multiple == 500) {
                    for (Subscription subs : list) {
                        if (subs.getId() % 500 == 0) {
                            String winnermsg = subscriptionFacade.winnerGenerator(subs.getIdStore(), subs.getId());
                            String resultSent = subscriptionFacade.sendEmail(subs.getEmail(), subs.getIdStore(), winnermsg);
                            sentCounter++;
                        }
                    }
                }
            } else if (multiple == 500) {
                for (Subscription subs : list) {
                    if (subs.getId() % 500 == 0) {
                        String winnermsg = subscriptionFacade.winnerGenerator(subs.getIdStore(), subs.getId());
                        String resultSent = subscriptionFacade.sendEmail(subs.getEmail(), subs.getIdStore(), winnermsg);
                        sentCounter++;
                    }
                }
            }
            if (sentCounter > 0) {
                return new GenericJSONResponse(true, GenericJSONResponse.SUCCESFUL_PROCESSED_STATUS, "Email sent to the winners").toString();
            } else {
                return new GenericJSONResponse(false, GenericJSONResponse.NOT_FOUND_RESOURCE_STATUS, "No result of winner found").toString();
            }
        } else {
            response = new GenericJSONResponse(false, GenericJSONResponse.INVALID_SESSION_STATUS, "No exist Session").toString();
        }
        return response;
    }

}
