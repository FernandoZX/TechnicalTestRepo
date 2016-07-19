/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.koombeaTest.facade;

import com.koombeaTest.Entities.Usuario;
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
public class UsuariosFacade extends AbstractFacade<Usuario> {

    @PersistenceContext(unitName = "SubscriptionServicesApiPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public UsuariosFacade() {
        super(Usuario.class);
    }
    
    
    public Usuario login(String userName,String password){
        Query q=getEntityManager().createQuery("SELECT us FROM Usuario us WHERE us.username=:username and us.password=:password");
        q.setParameter("username", userName);
        q.setParameter("password", password);
        List<Usuario> list=(List<Usuario>)q.getResultList();
        if(list.isEmpty()){
            return null;
        }
        return list.get(0);
    }
    
}
