/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.koombeaTest.GenericObjects;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 *
 * @author FernandoWinss7
 */
public class SessionManager {
    private HttpSession session;

    public SessionManager(HttpServletRequest request) {
        this.session = request.getSession(false);
    }
    
    public SessionManager(HttpServletRequest request, boolean create) {
        this.session = request.getSession(create);
    }

    public HttpSession getSession() {
        return session;
    }
    
    public boolean validateSession(){
        return this.session != null;
    }
    
    public void invalidateSession(){
        if (this.session != null){
            this.session.invalidate();
        }
    }
    
    public boolean closeSession(){
        boolean res;
        if (this.session == null){
           res = false;
        }else{
           session.invalidate();
           res = true;
        }
        return res;
    }
}
