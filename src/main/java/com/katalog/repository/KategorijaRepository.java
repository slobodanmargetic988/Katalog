/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.katalog.repository;

import com.katalog.model.Kategorija;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Slobodan
 */
@Repository
@Transactional
public class KategorijaRepository implements KategorijaDAO {
 @PersistenceContext
    private EntityManager manager;
     

 
    @Override
    public List<Kategorija> getallkategorije() {
        List<Kategorija> kategorije = manager.createNamedQuery("getallkategorije", Kategorija.class).getResultList();
        return kategorije;
    }
    
   
    @Override
    public Kategorija findFirstKategorijaById(int id) {
        Kategorija kategorija = manager.createNamedQuery("findFirstKategorijaById", Kategorija.class)
    .setParameter("inputid", id).getSingleResult();
        return kategorija;
    }

    
    
}
