/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.koombeaTest.facade;

import com.koombeaTest.Entities.Prizes;
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
public class PrizesFacade extends AbstractFacade<Prizes> {

    @PersistenceContext(unitName = "SubscriptionServicesApiPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public PrizesFacade() {
        super(Prizes.class);
    }
    public String createPrize(String name) {
        String result = "";
        try {
            Prizes prizes = new Prizes();
            prizes.setName(name);
            
            prizes.setActivo(true);
            getEntityManager().persist(prizes);
            result = "SUCCESS";
        } catch (Exception ex) {
            ex.printStackTrace();
            result = "Can't create the Prizes";
        }
        return result;
    }

    public String editPrizes(Prizes current, String name) {
        String result = "";
        try {
            current.setName(name);
            current.setActivo(true);
            getEntityManager().merge(current);
            result="SUCCESS";
        } catch (Exception ex) {
            ex.printStackTrace();
            result = "Can't edit the Prizes";
        }
        return result;
    }
    public String deletePrizes(Prizes current) {
        String result = "";
        try {
            current.setActivo(false);
            getEntityManager().merge(current);
            result="SUCCESS";
        } catch (Exception ex) {
            ex.printStackTrace();
            result = "Can't delete the Prizes";
        }
        return result;
    }

    public Prizes findPrizes(int prizeID) {
        Query q = getEntityManager().createQuery("SELECT p FROM Prizes p WHERE p.id=:prizeID and p.activo=TRUE");
        q.setParameter("prizeID", prizeID);
        if(2 % 500==0){
            System.out.println("winner");
        }
        List<Prizes> lst = q.getResultList();
        if (lst.isEmpty()) {
            return null;
        }
        return lst.get(0);
    }

    public List<Prizes> listPrizes(String name, int start, int limit) {
        Query q = getEntityManager().createQuery("SELECT p FROm Prizes p WHERE UPPER(p.name) like UPPER(:nombre) and p.activo=TRUE")
                .setFirstResult(start)
                .setMaxResults(limit);
        q.setParameter("nombre", "%" + name + "%");
        List<Prizes> lst = (List<Prizes>) q.getResultList();
        return lst;
    }

    public Long countPrizes(String name) {
        Long total;
        try {
            Query q = getEntityManager().createQuery("SELECT COUNT(p) FROm Prizes p WHERE UPPER(p.name) like UPPER(:nombre) and p.activo=TRUE");
            q.setParameter("nombre", "%" + name + "%");
            total = (Long) q.getSingleResult();
        } catch (Exception ex) {
            ex.printStackTrace();
            total = new Long("0");
        }
        return total;
    }
    
    
}
