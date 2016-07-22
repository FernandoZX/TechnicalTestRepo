/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.koombeaTest.models;

import java.util.Date;

/**
 *
 * @author fernando
 */
public class SubscriberModel {
    private Integer id;
    private String email;
    private Date registereddate;
    private String storename;
    private int storeid;
    public SubscriberModel(){
        
    }
    /**
     * @return the id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return the registereddate
     */
    public Date getRegistereddate() {
        return registereddate;
    }

    /**
     * @param registereddate the registereddate to set
     */
    public void setRegistereddate(Date registereddate) {
        this.registereddate = registereddate;
    }

    /**
     * @return the storename
     */
    public String getStorename() {
        return storename;
    }

    /**
     * @param storename the storename to set
     */
    public void setStorename(String storename) {
        this.storename = storename;
    }

    /**
     * @return the storeid
     */
    public int getStoreid() {
        return storeid;
    }

    /**
     * @param storeid the storeid to set
     */
    public void setStoreid(int storeid) {
        this.storeid = storeid;
    }
    
}
