/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.katalog.service;

import com.katalog.model.User;
import com.katalog.repository.UserRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Slobodan Margetic slobodanmargetic988@gmail.com
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<User> findAllBy() {
        return userRepository.findAllBy();
    }

    @Override
    public User findFirstByEmail(String Email) {
        return userRepository.findFirstByEmail(Email);
    }

    @Override
    public User findFirstByIme(String ime) {
        return userRepository.findFirstByIme(ime);
    }

    @Override
    public User findFirstByEmailAndPassword(String email, String lozinka) {
        return userRepository.findFirstByEmailAndPassword(email, lozinka);
    }

    @Override
    public void save(User user) {

        userRepository.save(user);
    }

    @Override
    public void saveAndFlush(User user) {

        userRepository.saveAndFlush(user);
    }

}
