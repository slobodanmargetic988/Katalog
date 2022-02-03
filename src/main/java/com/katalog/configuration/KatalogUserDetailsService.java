package com.katalog.configuration;

import com.katalog.configuration.KatalogUserPrincipal;
import com.katalog.model.User;
import com.katalog.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import org.springframework.stereotype.Service;
/**
 *
 * @author Slobodan Margetic slobodanmargetic988@gmail.com
 */
@Service
public class KatalogUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
 
        User user = userRepository.findFirstByEmail(username);
        if (user == null) {
            throw new UsernameNotFoundException(username);
        }
 
        return new KatalogUserPrincipal(user);
    }



}
