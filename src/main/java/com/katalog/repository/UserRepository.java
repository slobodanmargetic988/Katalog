/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.katalog.repository;

import com.katalog.model.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

/**
 *
 * @author Slobodan
 */


@Repository("userRepository")
public interface UserRepository extends JpaRepository<User, Integer> {

    List<User> findAllBy();
    User findFirstByEmail(String email);
    User findFirstByIme(String ime);
   User findFirstByEmailAndPassword(String email, String lozinka);
   // Notifikacije findFirstByOpstinaAndToken(  String opstina, String token);
    
   
}
