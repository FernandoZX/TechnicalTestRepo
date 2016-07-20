/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.koombeaTest.facade;

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
public class StoreFacade extends AbstractFacade<Store> {

    @PersistenceContext(unitName = "SubscriptionServicesApiPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public StoreFacade() {
        super(Store.class);
    }

    public String createStore(String name, String descripcion) {
        String result = "";
        try {
            Store store = new Store();
            store.setNombre(name);
            store.setDescripcion(descripcion);
            store.setActivo(true);
            getEntityManager().persist(store);
            result = "SUCCESS";
        } catch (Exception ex) {
            ex.printStackTrace();
            result = "Can't create the store";
        }
        return result;
    }

    public String editStore(Store current, String name, String descripcion) {
        String result = "";
        try {
            current.setNombre(name);
            current.setDescripcion(descripcion);
            current.setActivo(true);
            getEntityManager().merge(current);
            result="SUCCESS";
        } catch (Exception ex) {
            ex.printStackTrace();
            result = "Can't edit the store";
        }
        return result;
    }

    public Store findStore(int storeID) {
        Query q = getEntityManager().createQuery("SELECT s FROM Store s WHERE s.id=:storeID and s.activo=TRUE");
        q.setParameter("storeID", storeID);
        List<Store> lst = q.getResultList();
        if (lst.isEmpty()) {
            return null;
        }
        return lst.get(0);
    }

    public List<Store> listStore(String name, int start, int limit) {
        Query q = getEntityManager().createQuery("SELECT s FROm Store s WHERE UPPER(s.nombre) like UPPER(:nombre) and s.activo=TRUE")
                .setFirstResult(start)
                .setMaxResults(limit);
        q.setParameter("nombre", "%" + name + "%");
        List<Store> lst = (List<Store>) q.getResultList();
        return lst;
    }
    
    public List<Store> listStoreSecond(String name) {
        String temp=name!=null?name:"";
        Query q = getEntityManager().createQuery("SELECT s FROm Store s WHERE UPPER(s.nombre) like UPPER(:nombre) and s.activo=TRUE");
        q.setParameter("nombre", "%" + temp + "%");
        List<Store> lst = (List<Store>) q.getResultList();
        return lst;
    }
    public Long countStore(String name) {
        Long total;
        try {
            Query q = getEntityManager().createQuery("SELECT COUNT(s) FROm Store s WHERE UPPER(s.nombre) like UPPER(:nombre) and s.activo=TRUE");
            q.setParameter("nombre", "%" + name + "%");
            total = (Long) q.getSingleResult();
        } catch (Exception ex) {
            ex.printStackTrace();
            total = new Long("0");
        }
        return total;
    }
}
