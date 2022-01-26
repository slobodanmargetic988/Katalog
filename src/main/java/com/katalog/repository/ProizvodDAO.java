/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.katalog.repository;

import com.katalog.model.Proizvod;
import java.util.List;

/**
 *
 * @author Slobodan
 */
public interface ProizvodDAO {
       public List<Proizvod> getallproizvodi();
         public Proizvod findFirstById(int id);
       
}
