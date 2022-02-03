/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.katalog.repository;

import com.katalog.model.Proizvod;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Slobodan Margetic slobodanmargetic988@gmail.com
 */
@Repository
@Transactional
public class ProizvodRepository implements ProizvodDAO {
 @PersistenceContext
    private EntityManager manager;
     

 
    @Override
    public List<Proizvod> getallproizvodi() {
        List<Proizvod> proizvodi = manager.createNamedQuery("getallproizvodi", Proizvod.class).getResultList();
        return proizvodi;
    }
    
    
    @Override
    public Proizvod findFirstById(int id) {
        Proizvod proizvod = manager.createNamedQuery("findFirstById", Proizvod.class)
    .setParameter("inputid", id).getSingleResult();
        return proizvod;
    }

       
    @Override
    public List<Proizvod> pretragaPoImenu(String naziv) {
        List<Proizvod> proizvodi = manager.createNamedQuery("pretragaPoImenu", Proizvod.class).setParameter("inputnaziv", "%"+naziv+"%").getResultList();
        return proizvodi;
    } 
        
}
