package com.demo.nutgrow.config;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

public class SecurityUser extends User {

    private static final long serialVersionUID = 1L;
    private String email;
    private String provider;

    public SecurityUser(String username, String password, boolean enabled, boolean accountNonExpired,
                        boolean credentialsNonExpired, boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities,
                        String email, String provider) {
        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
        this.email = email;
        this.provider = provider;
    }

    @Override
    public String toString() {
        return "MySecurityUser[email=" + email
                + "] " + super.toString();
    }
}