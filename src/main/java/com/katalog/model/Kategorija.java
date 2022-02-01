/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.katalog.model;

import java.util.List;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.Table;

/**
 *
 * @author Slobodan
 */
@Entity(name="Kategorija")
@Table(name = "kategorija",schema="webkatalog")
@NamedNativeQueries({
    @NamedNativeQuery(
            name    =   "getallkategorije",
            query   =   "SELECT id, naziv, opis FROM webkatalog.kategorija",
                        resultClass = Kategorija.class
    ),
            @NamedNativeQuery(
            name    =   "findFirstKategorijaById",
            query   =   "SELECT id, naziv, opis FROM webkatalog.kategorija k where k.id= :inputid",
       
                        resultClass = Kategorija.class
    )
})
public class Kategorija {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "naziv")
    private String naziv;

    @Column(name = "opis")
    private String opis;

    
/*    @ManyToMany(cascade = {CascadeType.ALL})
    @JoinTable(
            name = "veznatabela",
            joinColumns = {
                @JoinColumn(name = "id_kategorije")},
            inverseJoinColumns = {
                @JoinColumn(name = "id_proizvoda")}
    )
        private List<Proizvod> proizvodi;*/
@ManyToMany(mappedBy = "kategorija")
    private  List<VeznaTabela> veznatabela;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public String getOpis() {
        return opis;
    }

    public void setOpis(String opis) {
        this.opis = opis;
    }

    public List<VeznaTabela> getVeznatabela() {
        return veznatabela;
    }

    public void setVeznatabela(List<VeznaTabela> veznatabela) {
        this.veznatabela = veznatabela;
    }

 


    
}
