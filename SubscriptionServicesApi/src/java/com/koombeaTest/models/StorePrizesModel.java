/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.koombeaTest.models;

/**
 *
 * @author fernando
 */
public class StorePrizesModel {
    private int storeID;
    private String prizes;
    private String storeName;

    public StorePrizesModel() {
    }

    public StorePrizesModel(String prizes, String storeName) {
        this.prizes = prizes;
        this.storeName = storeName;
    }

    /**
     * @return the email
     */
    public String getPrizes() {
        return prizes;
    }

    /**
     * @param email the email to set
     */
    public void setPrizes(String prizes) {
        this.prizes = prizes;
    }

    /**
     * @return the storeName
     */
    public String getStoreName() {
        return storeName;
    }

    /**
     * @param storeName the storeName to set
     */
    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    /**
     * @return the storeID
     */
    public int getStoreID() {
        return storeID;
    }

    /**
     * @param storeID the storeID to set
     */
    public void setStoreID(int storeID) {
        this.storeID = storeID;
    }

}
