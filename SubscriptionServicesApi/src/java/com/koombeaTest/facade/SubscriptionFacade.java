/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.koombeaTest.facade;

import com.koombeaTest.Entities.Store;
import com.koombeaTest.Entities.SubsPrizesinventory;
import com.koombeaTest.Entities.Subscription;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.HtmlEmail;

/**
 *
 * @author fernando
 */
@Stateless
public class SubscriptionFacade extends AbstractFacade<Subscription> {

    @PersistenceContext(unitName = "SubscriptionServicesApiPU")
    private EntityManager em;
    @EJB
    private PrizesInventoryFacade inventoryFacade;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public SubscriptionFacade() {
        super(Subscription.class);
    }

    public String createSubscription(String email, Store store, Date now) {
        String result = "";
        try {
            Subscription subscription = new Subscription();
            subscription.setEmailSubscriber(email);
            subscription.setIdStore(store);
            subscription.setRegisteredDate(now);
            subscription.setActivo(true);
            getEntityManager().persist(subscription);
            String  emailResult=sendEmail(email, store);
        } catch (Exception ex) {
            ex.printStackTrace();
            result = "Can't create a subscription";
        }
        return result;
    }

    public String sendEmail(String subscriberEmail, Store store) {
        String result = "";
        try {
            StringBuilder builder= new StringBuilder();
            
            HtmlEmail email = new HtmlEmail();
            email.setHostName("smtp.gmail.com");
            email.setSmtpPort(465);
            email.setAuthenticator(new DefaultAuthenticator("ingfernandoc@gmail.com", "d4n13ll4"));
            email.setSSLOnConnect(true);
            email.setFrom("Senders' email");
            email.setSubject("Congrats! - You subscribe " + store.getNombre());
            email.setHtmlMsg("<html>\n"
                    + "<body>\n"
                    + "\n"
                    + "<a href=\"http://pushpalankajaya.blogspot.com\">\n"
                    + "This is a link</a>\n"
                    + "\n"
                    + "</body>\n"
                    + "</html>");
            // set the alternative message
            email.addTo(subscriberEmail);
            email.send();
            result = "SUCCESS";
        } catch (Exception ex) {
            ex.printStackTrace();
            result = "Can't Send the email contact to the administrator";
        }
        return result;
    }

    public Long countSubscriptionByStore(Store store) {
        Long total;
        try {
            Query q = getEntityManager().createQuery("SELECT subs FROM Subscription subs WHERE SUBS.idStore.id=:storeID and SUBS.activo=TRUE");
            q.setParameter("storeID", store.getId());
            total = (Long) q.getSingleResult();
        } catch (Exception ex) {
            ex.printStackTrace();
            total = new Long("0");
        }
        return total;
    }

    public Subscription findSubscriptionByStore(Integer storeID) {
        Query q = getEntityManager().createQuery("SELECT subs FROM Subscription subs WHERE subs.idStore.id=:idStore and subs.activo=TRUE");
        q.setParameter("idStore", storeID);
        List<Subscription> list = q.getResultList();
        if (list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }

    public boolean validateDateSubs(String email, Store store, Date now) {
        Query q = getEntityManager().createQuery("SELECT subs FROM Subscription subs WHERE subs.emailSubscriber=:email and subs.registeredDate=:date and subs.idStore.id=:idStore and subs.activo=TRUE");
        q.setParameter("email", email);
        q.setParameter("date", now);
        q.setParameter("idStore", store.getId());
        List<Subscription> list = q.getResultList();
        if (list.isEmpty()) {
            return false;
        }
        return true;
    }
    /*
    public List<Subscription> listWinner(int numberOfMultiple){
        
    }*/
}
