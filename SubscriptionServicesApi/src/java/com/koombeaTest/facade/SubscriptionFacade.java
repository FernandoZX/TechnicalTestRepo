/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.koombeaTest.facade;

import com.koombeaTest.Entities.Store;
import com.koombeaTest.Entities.SubsPrizesinventory;
import com.koombeaTest.Entities.Subscription;
import com.koombeaTest.Services.InventoryResource;
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
            String winnerMsg = "";
            Subscription subscription = new Subscription();
            subscription.setEmail(email);
            subscription.setIdStore(store);
            subscription.setRegistereddate(now);
            subscription.setActivo(true);
            getEntityManager().persist(subscription);
            Subscription winner = findSubscriptionByStore(store.getId());
            Long countSubscriberByStore = countSubscriptionByStore(store);
            if (winner.getId() == 50) {
                winnerMsg = winnerGenerator(store, winner.getId());
            }
            if (winner.getId() == 100) {
                winnerMsg = winnerGenerator(store, winner.getId());
            }
            if (winner.getId() == 2000) {
                winnerMsg = winnerGenerator(store, winner.getId());
            }
            if (winner.getId() % 500 == 0) {
                System.out.println("winner");
                winnerMsg = winnerGenerator(store, winner.getId());
                // j is an exact multiple of 4
            }
            if (countSubscriberByStore > 1000) {
                if (winner.getId() % 50 == 0) {
                    winnerMsg = winnerGenerator(store, winner.getId());
                }
            }
            String emailResult = sendEmail(email, store, winnerMsg);
            result = emailResult;
        } catch (Exception ex) {
            ex.printStackTrace();
            result = "Can't create a subscription";
        }
        return result;
    }

    public String sendEmail(String subscriberEmail, Store store, String msgWinner) {
        String result = "";
        try {
            StringBuilder builder = new StringBuilder();
            List<SubsPrizesinventory> list = inventoryFacade.findPrizeInventoryByStore(store.getId());
            builder.append("<ul>");
            for (SubsPrizesinventory subsPrizesinventory : list) {
                builder.append("<li>" + subsPrizesinventory.getIdPrize().getName() + "</li>");
            }
            builder.append("</ul>");
            String msgResult = msgWinner.isEmpty() ? "<html>\n"
                    + "<body>\n"
                    + "\n"
                    + "<strong>" + store.getNombre() + "</strong>\n"
                    + "<br/>\n"
                    + builder.toString()
                    + "\n"
                    + "</body>\n"
                    + "</html>" : msgWinner;
            HtmlEmail email = new HtmlEmail();
            email.setHostName("smtp.gmail.com");
            email.setSmtpPort(587);
            email.setAuthenticator(new DefaultAuthenticator("email here", "password here"));
            email.setStartTLSRequired(true);
            email.setFrom("email here", "Subscriber sw");
            email.setSubject("Congrats! - You subscribe " + store.getNombre());
            email.setHtmlMsg(msgResult);
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

    public List<Subscription> listSubscriptionByStore(String storeName, int start, int limit) {
        Query q = getEntityManager().createQuery("SELECT subs FROM Subscription subs WHERE UPPER(subs.idStore.nombre) like UPPER(:nombre) and subs.activo=TRUE ORDER BY subs.registereddate DESC").setFirstResult(start).setMaxResults(limit);
        q.setParameter("nombre", "%" + storeName + "%");
        List<Subscription> list = q.getResultList();
        return list;
    }
    public List<Subscription> listSubscriptionByStore(Store store, int start, int limit) {
        Query q = getEntityManager().createQuery("SELECT subs FROM Subscription subs WHERE subs.idStore.id=:storeID and subs.activo=TRUE ORDER BY subs.registereddate DESC").setFirstResult(start).setMaxResults(limit);
        q.setParameter("storeID", store.getId());
        List<Subscription> list = q.getResultList();
        return list;
    }
    public List<Subscription> listSubscriptionByStore(String storeName) {
        Query q = getEntityManager().createQuery("SELECT subs FROM Subscription subs WHERE UPPER(subs.idStore.nombre) like UPPER(:nombre) and subs.activo=TRUE ORDER BY subs.registereddate DESC");
        q.setParameter("nombre", "%" + storeName + "%");
        List<Subscription> list = q.getResultList();
        return list;
    }
    public Long countSubscriptionByStore(String storeName) {
        Long total;
        try {
            Query q = getEntityManager().createQuery("SELECT count(subs) FROM Subscription subs WHERE UPPER(SUBS.idStore.nombre) like UPPER(:nombre) and SUBS.activo=TRUE");
            q.setParameter("nombre", "%" + storeName + "%");
            total = (Long) q.getSingleResult();
        } catch (Exception ex) {
            ex.printStackTrace();
            total = new Long("0");
        }
        return total;
    }

    public Long countSubscriptionByStore(Store store) {
        Long total;
        try {
            Query q = getEntityManager().createQuery("SELECT count(subs) FROM Subscription subs WHERE SUBS.idStore.id = :storeID and SUBS.activo=TRUE");
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
        Query q = getEntityManager().createQuery("SELECT subs FROM Subscription subs WHERE UPPER(subs.email)=UPPER(:email) and subs.registereddate=:date and subs.idStore.id=:idStore and subs.activo=TRUE");
        q.setParameter("email", email);
        q.setParameter("date", now);
        q.setParameter("idStore", store.getId());
        List<Subscription> list = q.getResultList();
        if (list.isEmpty()) {
            return false;
        }
        return true;
    }

    public String winnerGenerator(Store store, int subscriberNumber) {
        String winnerMsg = "";
        StringBuilder builder = new StringBuilder();
        List<SubsPrizesinventory> list = inventoryFacade.findPrizeInventoryByStore(store.getId());
        builder.append("<ul>");
        for (SubsPrizesinventory subsPrizesinventory : list) {
            builder.append("<li>" + subsPrizesinventory.getIdPrize().getName() + "</li>");
            SubsPrizesinventory found = inventoryFacade.findPrizeInventoryByStoreAndPrizes(store.getId(), subsPrizesinventory.getId());
            int resultChange = found.getStock() - 1;
            found.setStock(resultChange);
            inventoryFacade.editStock(found);
        }
        builder.append("</ul>");
        winnerMsg = "<html>\n"
                + "<body>\n"
                + "\n"
                + "<strong> Congrats! You are the Subscriber Nr50 of " + store.getNombre() + "</strong>\n"
                + "<br/>\n"
                + "you won this prizes below in the list:"
                + builder.toString()
                + "\n"
                + "</body>\n"
                + "</html>";
        return winnerMsg;
    }
    
}
