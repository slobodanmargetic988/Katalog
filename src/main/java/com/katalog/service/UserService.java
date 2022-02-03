/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.katalog.service;

import com.katalog.model.User;
import java.util.List;

/**
 *
 * @author Slobodan Margetic slobodanmargetic988@gmail.com
 */
public interface UserService {

    List<User> findAllBy();

    User findFirstByEmail(String Email);
  User findFirstByIme(String ime);
    User findFirstByEmailAndPassword(String email, String lozinka);

    void save(User user);
        void saveAndFlush(User user);

}
