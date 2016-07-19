/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.koombeaTest.Entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author fernando
 */
@Entity
@Table(name = "subs_prizesinventory",schema="subscriber")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SubsPrizesinventory.findAll", query = "SELECT s FROM SubsPrizesinventory s"),
    @NamedQuery(name = "SubsPrizesinventory.findById", query = "SELECT s FROM SubsPrizesinventory s WHERE s.id = :id"),
    @NamedQuery(name = "SubsPrizesinventory.findByStock", query = "SELECT s FROM SubsPrizesinventory s WHERE s.stock = :stock"),
    @NamedQuery(name = "SubsPrizesinventory.findByActivo", query = "SELECT s FROM SubsPrizesinventory s WHERE s.activo = :activo")})
public class SubsPrizesinventory implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "stock")
    private int stock;
    @Basic(optional = false)
    @NotNull
    @Column(name = "activo")
    private boolean activo;
    @JoinColumn(name = "id_prize", referencedColumnName = "id")
    @ManyToOne
    private Prizes idPrize;
    @JoinColumn(name = "id_store", referencedColumnName = "id")
    @ManyToOne
    private Store idStore;

    public SubsPrizesinventory() {
    }

    public SubsPrizesinventory(Integer id) {
        this.id = id;
    }

    public SubsPrizesinventory(Integer id, int stock, boolean activo) {
        this.id = id;
        this.stock = stock;
        this.activo = activo;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public boolean getActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public Prizes getIdPrize() {
        return idPrize;
    }

    public void setIdPrize(Prizes idPrize) {
        this.idPrize = idPrize;
    }

    public Store getIdStore() {
        return idStore;
    }

    public void setIdStore(Store idStore) {
        this.idStore = idStore;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SubsPrizesinventory)) {
            return false;
        }
        SubsPrizesinventory other = (SubsPrizesinventory) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.koombeaTest.Entities.SubsPrizesinventory[ id=" + id + " ]";
    }
    
}
