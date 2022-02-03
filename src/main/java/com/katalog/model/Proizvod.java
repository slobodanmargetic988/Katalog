/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.katalog.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.NamedStoredProcedureQuery;
import javax.persistence.OneToMany;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureParameter;
import javax.persistence.Table;

/**
 *
 * @author Slobodan Margetic slobodanmargetic988@gmail.com
 */
@Entity(name = "Proizvod")
@Table(name = "proizvod", schema = "webkatalog")
@NamedNativeQueries({
    @NamedNativeQuery(
            name = "getallproizvodi",
            query = "SELECT id, naziv, opis, cena, dostupna_kolicina FROM webkatalog.proizvod",
            /*  "SELECT proizvod.id,naziv,opis,cena,dostupna_kolicina,id_kategorije FROM webkatalog.proizvod inner join webkatalog.veznatabela on webkatalog.proizvod.id = webkatalog.veznatabela.id_proizvoda",*/
            resultClass = Proizvod.class
    )
    ,
            @NamedNativeQuery(
            name = "findFirstById",
            query = "SELECT id, naziv, opis, cena, dostupna_kolicina FROM webkatalog.proizvod p where p.id= :inputid",
            resultClass = Proizvod.class
    )
    ,
            @NamedNativeQuery(
            name = "pretragaPoImenu",
            query = "SELECT id, naziv, opis, cena, dostupna_kolicina FROM webkatalog.proizvod p where p.naziv LIKE :inputnaziv",
            resultClass = Proizvod.class
    )
})

public class Proizvod implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    //  @Column(name = "id")
    private int id;

    //  @Column(name = "naziv")
    private String naziv;

    //   @Column(name = "opis")
    private String opis;

//    @Column(name = "cena")
    private int cena;

    @Column(name = "dostupna_kolicina")
    private int dostupno;

    @OneToMany(mappedBy = "proizvod")
    private List<VeznaTabela> veznatabela = new ArrayList<>();

    public Proizvod() {
    }

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

    public List<VeznaTabela> getVeznatabela() {
        return veznatabela;
    }

    public void setVeznatabela(List<VeznaTabela> veznatabela) {
        this.veznatabela = veznatabela;
    }

}
