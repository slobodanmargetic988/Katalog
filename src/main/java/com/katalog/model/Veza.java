/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.katalog.model;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.NamedStoredProcedureQuery;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureParameter;
import javax.persistence.Table;


/**
 *
 * @author Slobodan
 */


@Entity(name="Veza")
@Table (name="veznatabela")
@NamedNativeQueries({
    @NamedNativeQuery(
            name    =   "getallveze",
            query   =   "SELECT id, id_proizvoda, id_kategorije FROM webkatalog.proizvod",
                        resultClass = Proizvod.class
    )
})
public class Veza implements Serializable{
private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

  
    @ManyToOne
    private Proizvod proizvod1;
   
    @ManyToOne
    private Kategorija kategorija;

    public Veza() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Proizvod getProizvod1() {
        return proizvod1;
    }

    public void setProizvod1(Proizvod proizvod1) {
        this.proizvod1 = proizvod1;
    }

    public Kategorija getKategorija() {
        return kategorija;
    }

    public void setKategorija(Kategorija kategorija) {
        this.kategorija = kategorija;
    }

   
    
    
    
}
