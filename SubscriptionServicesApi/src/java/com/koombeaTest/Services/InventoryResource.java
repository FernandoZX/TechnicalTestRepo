/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.koombeaTest.Services;

import com.koombeaTest.Entities.Store;
import com.koombeaTest.Entities.Prizes;
import com.koombeaTest.Entities.SubsPrizesinventory;
import com.koombeaTest.GenericObjects.GenericJSONResponse;
import com.koombeaTest.GenericObjects.SessionManager;
import com.koombeaTest.facade.PrizesFacade;
import com.koombeaTest.facade.PrizesInventoryFacade;
import com.koombeaTest.facade.StoreFacade;
import com.koombeaTest.models.StorePrizesModel;
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
@Path("inventory")
public class InventoryResource {

    @Context
    private UriInfo context;
    @EJB
    private PrizesInventoryFacade prizesInventoryFacade;
    @EJB
    private StoreFacade storeFacade;
    @EJB
    private PrizesFacade prizesFacade;

    /**
     * Creates a new instance of InventoryResource
     */
    public InventoryResource() {
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("crear")
    public String create(@Context HttpServletRequest httpServletRequest, @FormParam("storeID") Integer storeID, @FormParam("prizesID") Integer prizesID, @FormParam("stock") Integer stock) {
        String response = "";
        SessionManager sessionManager = new SessionManager(httpServletRequest);
        if (sessionManager.validateSession()) {
            if (storeID == null) {
                return new GenericJSONResponse(false, GenericJSONResponse.CONFLICT_STATUS, "Store ID required").toString();
            }
            if (prizesID == null) {
                return new GenericJSONResponse(false, GenericJSONResponse.CONFLICT_STATUS, "Prizes ID required").toString();
            }
            if (stock == null) {
                return new GenericJSONResponse(false, GenericJSONResponse.CONFLICT_STATUS, "stock required").toString();
            }
            Store store = storeFacade.findStore(storeID);
            if (store == null) {
                return new GenericJSONResponse(false, GenericJSONResponse.NOT_FOUND_RESOURCE_STATUS, "Store no found by this ID: " + storeID).toString();
            }
            Prizes prizes = prizesFacade.findPrizes(prizesID);
            if (prizes == null) {
                return new GenericJSONResponse(false, GenericJSONResponse.NOT_FOUND_RESOURCE_STATUS, "Prizes no found by this ID: " + prizesID).toString();
            }
            boolean validateExistence = prizesInventoryFacade.validatePrizeInventory(store, prizes);
            if (validateExistence) {
                return new GenericJSONResponse(false, GenericJSONResponse.CONFLICT_STATUS, "This prizes is already registered").toString();
            }
            response = prizesInventoryFacade.createPrizeInventory(store, prizes, stock);
            if (response.equalsIgnoreCase("SUCCESS")) {
                response = new GenericJSONResponse(true, GenericJSONResponse.SUCCESFUL_PROCESSED_STATUS, "Prize registered into the inventory successful").toString();
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
    public String edit(@Context HttpServletRequest httpServletRequest, @FormParam("inventoryID") Integer inventoryID, @FormParam("storeID") Integer storeID, @FormParam("prizesID") Integer prizesID, @FormParam("stock") Integer stock) {
        String response = "";
        SessionManager sessionManager = new SessionManager(httpServletRequest);
        if (sessionManager.validateSession()) {
            if (inventoryID == null) {
                return new GenericJSONResponse(false, GenericJSONResponse.CONFLICT_STATUS, "inventory ID required").toString();
            }
            if (storeID == null) {
                return new GenericJSONResponse(false, GenericJSONResponse.CONFLICT_STATUS, "Store ID required").toString();
            }
            if (prizesID == null) {
                return new GenericJSONResponse(false, GenericJSONResponse.CONFLICT_STATUS, "Prizes ID required").toString();
            }
            if (stock == null) {
                return new GenericJSONResponse(false, GenericJSONResponse.CONFLICT_STATUS, "stock required").toString();
            }
            SubsPrizesinventory prizesInventory = prizesInventoryFacade.findPrizeInventory(inventoryID);
            if (prizesInventory == null) {
                return new GenericJSONResponse(false, GenericJSONResponse.NOT_FOUND_RESOURCE_STATUS, "Inventory Record no found by this ID: " + storeID).toString();
            }
            Store store = storeFacade.findStore(storeID);
            if (store == null) {
                return new GenericJSONResponse(false, GenericJSONResponse.NOT_FOUND_RESOURCE_STATUS, "Store no found by this ID: " + storeID).toString();
            }
            Prizes prizes = prizesFacade.findPrizes(prizesID);
            if (prizes == null) {
                return new GenericJSONResponse(false, GenericJSONResponse.NOT_FOUND_RESOURCE_STATUS, "Prizes no found by this ID: " + prizesID).toString();
            }

            response = prizesInventoryFacade.editPrizeInventory(prizesInventory, store, prizes, stock);
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
    public String list(@Context HttpServletRequest httpServletRequest, @FormParam("storeKey") String storeKey, @FormParam("prizeKey") String prizeKey, @FormParam("start") int start, @FormParam("limit") int limit) {
        String response = "";
        SessionManager sessionManager = new SessionManager(httpServletRequest);
        if (sessionManager.validateSession()) {
            List<SubsPrizesinventory> list = prizesInventoryFacade.listPrizesInventory(storeKey, prizeKey, start, limit);
            Integer total = prizesInventoryFacade.countPrizesInventory(storeKey, prizeKey).intValue();
            return new GenericJSONResponse(true, GenericJSONResponse.SUCCESFUL_PROCESSED_STATUS, "Inventory lists", list, total).toString();
        } else {
            response = new GenericJSONResponse(false, GenericJSONResponse.INVALID_SESSION_STATUS, "No exist Session").toString();
        }
        return response;
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("delete")
    public String delete(@Context HttpServletRequest httpServletRequest, @FormParam("inventoryID") Integer inventoryID) {
        String response = "";
        SessionManager sessionManager = new SessionManager(httpServletRequest);
        if (sessionManager.validateSession()) {
            if (inventoryID == null) {
                return new GenericJSONResponse(false, GenericJSONResponse.CONFLICT_STATUS, "Inventory ID required").toString();
            }
            SubsPrizesinventory prizesinventory = prizesInventoryFacade.findPrizeInventory(inventoryID);
            if (prizesinventory == null) {
                return new GenericJSONResponse(false, GenericJSONResponse.CONFLICT_STATUS, "Inventory no found by this ID: " + inventoryID).toString();
            }
            if (prizesinventory.getStock() > 0) {
                return new GenericJSONResponse(false, GenericJSONResponse.CONFLICT_STATUS, "Cannot delete because have stock in this record").toString();
            }
            response = prizesInventoryFacade.deletePrizeInventory(prizesinventory);
            if (response.equalsIgnoreCase("SUCCESS")) {
                response = new GenericJSONResponse(true, GenericJSONResponse.SUCCESFUL_PROCESSED_STATUS, "Inventory record deleted successful").toString();
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
    @Path("listStorePrize")
    public String listStorePrize(@Context HttpServletRequest httpServletRequest, @FormParam("nombre") String name, @FormParam("start") int start, @FormParam("limit") int limit) {
        List<Store> list = storeFacade.listStoreSecond(name != null ? name : "");
        List<String> prizes = new LinkedList<String>();
        List<StorePrizesModel> modelList = new LinkedList<StorePrizesModel>();

        for (Store store : list) {
            List<SubsPrizesinventory> subLst = prizesInventoryFacade.findPrizeInventoryByStore(store.getId(), start, limit);
            for (SubsPrizesinventory subsPrizesinventory : subLst) {
                prizes.add(subsPrizesinventory.getIdPrize().getName());
            }
            StorePrizesModel subscriberModel = new StorePrizesModel();
            subscriberModel.setStoreID(store.getId());
            subscriberModel.setStoreName(store.getNombre());
            subscriberModel.setPrizes(!subLst.isEmpty() ? String.join(",", prizes) : "No prizes here");
            modelList.add(subscriberModel);
        }
        return new GenericJSONResponse(true, GenericJSONResponse.SUCCESFUL_PROCESSED_STATUS, "list store prizes", modelList, modelList.size()).toString();
    }
}
