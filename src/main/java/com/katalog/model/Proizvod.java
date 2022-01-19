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
import javax.persistence.Table;

/**
 *
 * @author Slobodan
 */
@Entity
@Table(name = "proizvod",schema="webkatalog")
public class Proizvod {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "naziv")
    private String naziv;

    @Column(name = "opis")
    private String opis;

    @Column(name = "cena")
    private int cena;

    @Column(name = "dostupna_kolicina")
    private int dostupno;
    
    @ManyToMany(cascade = {CascadeType.ALL})
    @JoinTable(
            name = "veznatabela",
            joinColumns = {
                @JoinColumn(name = "id_proizvoda")},
            inverseJoinColumns = {
                @JoinColumn(name = "id_kategorije")}
    )
        private List<Kategorija> kategorije;

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

    public int getCena() {
        return cena;
    }

    public void setCena(int cena) {
        this.cena = cena;
    }

    public int getDostupno() {
        return dostupno;
    }

    public void setDostupno(int dostupno) {
        this.dostupno = dostupno;
    }

    public List<Kategorija> getKategorije() {
        return kategorije;
    }

    public void setKategorije(List<Kategorija> kategorije) {
        this.kategorije = kategorije;
    }


    
    
}
