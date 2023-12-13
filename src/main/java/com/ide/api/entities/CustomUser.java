package com.ide.api.entities;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

public class CustomUser extends User {
    private final boolean isAdmin;

    public CustomUser(String username, String password, Collection<? extends GrantedAuthority> authorities, boolean isAdmin) {
        super(username, password, authorities);
        this.isAdmin = isAdmin;
    }

    public boolean isAdmin() {
        return isAdmin;
    }
}

