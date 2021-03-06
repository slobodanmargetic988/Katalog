package com.katalog.configuration;

import com.katalog.model.User;
import java.util.Collection;
import java.util.LinkedList;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 *
 * @author Slobodan Margetic slobodanmargetic988@gmail.com
 */

public class KatalogUserPrincipal implements UserDetails {

    private User user;
    private Collection<SimpleGrantedAuthority> authorities = new LinkedList<>();

    public KatalogUserPrincipal() {
        super();
    }

    public KatalogUserPrincipal(User user) {
        this.user = user;

        if (user.getRole().equals("ADMIN")) {
            authorities.add(new SimpleGrantedAuthority("ADMIN"));
        } else {
            if (user.getRole().equals("SHOPPER")) {
                authorities.add(new SimpleGrantedAuthority("SHOPPER"));
            } else {
                authorities.add(new SimpleGrantedAuthority("VISITOR"));
            }
        }
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public User getUser() {
        return user;
    }
}
