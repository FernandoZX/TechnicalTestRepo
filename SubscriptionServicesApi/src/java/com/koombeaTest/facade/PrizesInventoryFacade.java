/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.koombeaTest.facade;

import com.koombeaTest.Entities.Prizes;
import com.koombeaTest.Entities.SubsPrizesinventory;
import com.koombeaTest.Entities.Store;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author fernando
 */
@Stateless
public class PrizesInventoryFacade extends AbstractFacade<SubsPrizesinventory> {

    @PersistenceContext(unitName = "SubscriptionServicesApiPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public PrizesInventoryFacade() {
        super(SubsPrizesinventory.class);
    }

    public String createPrizeInventory(Store storeID, Prizes prizeID, Integer stock) {
        String result = "";
        try {
            SubsPrizesinventory prizesInventory = new SubsPrizesinventory();
            prizesInventory.setIdPrize(prizeID);
            prizesInventory.setIdStore(storeID);
            prizesInventory.setStock(stock);
            prizesInventory.setActivo(true);
            getEntityManager().persist(prizesInventory);
            result = "SUCCESS";
        } catch (Exception ex) {
            ex.printStackTrace();
            result = "Can't create prize inventory";
        }
        return result;
    }

    public String editPrizeInventory(SubsPrizesinventory current, Store storeID, Prizes prizeID, Integer stock) {
        String result = "";
        try {
            current.setIdPrize(prizeID);
            current.setIdStore(storeID);
            current.setStock(stock);
            current.setActivo(true);
            getEntityManager().merge(current);
            result = "SUCCESS";
        } catch (Exception ex) {
            ex.printStackTrace();
            result = "Can't edit prize inventory";
        }
        return result;
    }

    public void editStock(SubsPrizesinventory current) {
        try {
            getEntityManager().merge(current);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public String deletePrizeInventory(SubsPrizesinventory current) {
        String result = "";
        try {
            current.setActivo(false);
            getEntityManager().merge(current);
            result = "SUCCESS";
        } catch (Exception ex) {
            ex.printStackTrace();
            result = "Can't delete prize inventory";
        }
        return result;
    }

    public List<SubsPrizesinventory> listPrizesInventory(String storeKey, String prizeKey, int start, int limit) {
        Query q = getEntityManager().createQuery("SELECT pi FROM SubsPrizesinventory pi WHERE UPPER(pi.idStore.nombre) LIKE UPPER(:storeKey) and UPPER(pi.idPrize.name) like UPPER(:prizeKey) and pi.activo=TRUE")
                .setFirstResult(start)
                .setMaxResults(limit);
        q.setParameter("storeKey", "%" + storeKey + "%");
        q.setParameter("prizeKey", "%" + prizeKey + "%");
        List<SubsPrizesinventory> list = q.getResultList();
        return list;
    }

    public Long countPrizesInventory(String storeKey, String prizeKey) {
        Long total;
        try {
            Query q = getEntityManager().createQuery("SELECT COUNT(pi) FROM SubsPrizesinventory pi WHERE UPPER(pi.idStore.nombre) LIKE UPPER(:storeKey) and UPPER(pi.idPrize.name) like UPPER(:prizeKey) and pi.activo=TRUE");
            q.setParameter("storeKey", "%" + storeKey + "%");
            q.setParameter("prizeKey", "%" + prizeKey + "%");
            total = (Long) q.getSingleResult();
        } catch (Exception ex) {
            ex.printStackTrace();
            total = new Long("0");
        }
        return total;
    }

    public boolean validatePrizeInventory(Store storeID, Prizes prizesID) {
        Query q = getEntityManager().createQuery("SELECT pi FROM SubsPrizesinventory pi WHERE pi.idPrize.id=:idPrize and pi.idStore.id=:idStore and pi.activo=TRUE");
        q.setParameter("idPrize", prizesID.getId());
        q.setParameter("idStore", storeID.getId());
        List<SubsPrizesinventory> lst = q.getResultList();
        if (lst.isEmpty()) {
            return false;
        }
        return true;
    }

    public Long countSimilitudes(Store storeID, Prizes prizesID) {
        Long total;
        try {
            Query q = getEntityManager().createQuery("SELECT COUNT(pi) FROM SubsPrizesinventory pi WHERE pi.idPrize.id=:idPrize and pi.idStore.id=:idStore and pi.activo=TRUE");
            q.setParameter("idPrize", prizesID.getId());
            q.setParameter("idStore", storeID.getId());
            total = (Long) q.getSingleResult();
        } catch (Exception ex) {
            total = new Long("0");
        }
        return total;
    }

    public SubsPrizesinventory findPrizeInventory(int id) {
        Query q = getEntityManager().createQuery("SELECT pi FROM SubsPrizesinventory pi WHERE pi.id=:piID and pi.activo=TRUE");
        q.setParameter("piID", id);
        List<SubsPrizesinventory> list = q.getResultList();
        if (list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }

    public SubsPrizesinventory findPrizeInventoryByStoreAndPrizes(int idStore, int idPrizes) {
        Query q = getEntityManager().createQuery("SELECT pi FROM SubsPrizesinventory pi WHERE pi.idStore.id =:storeID and pi.idPrize.id=:prizeID and pi.activo=TRUE");
        q.setParameter("storeID", idStore);
        q.setParameter("prizeID", idPrizes);
        List<SubsPrizesinventory> list = q.getResultList();
        if (list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }

    public List<SubsPrizesinventory> findPrizeInventoryByStore(int id, int start, int limit) {
        Query q = getEntityManager().createQuery("SELECT pi FROM SubsPrizesinventory pi WHERE pi.idStore.id=:piID and pi.activo=TRUE").setFirstResult(start)
                .setMaxResults(limit);;
        q.setParameter("piID", id);
        List<SubsPrizesinventory> list = q.getResultList();

        return list;
    }

    public List<SubsPrizesinventory> findPrizeInventoryByStore(int id) {
        Query q = getEntityManager().createQuery("SELECT pi FROM SubsPrizesinventory pi WHERE pi.idStore.id=:piID and pi.activo=TRUE");
        q.setParameter("piID", id);
        List<SubsPrizesinventory> list = q.getResultList();

        return list;
    }
}
