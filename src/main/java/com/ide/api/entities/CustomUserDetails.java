package com.ide.api.entities;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

public class CustomUserDetails extends Utilisateur implements UserDetails {

    private Integer id;
    private String username;
    private String password;
    private Collection<? extends GrantedAuthority> authorities;

    public CustomUserDetails(Integer id,
                             String username,
                             String password,
                             Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.authorities = authorities;
        List<GrantedAuthority> auths = new ArrayList<>();
    }

    public static  CustomUserDetails build(Utilisateur utilisateur){
        List<GrantedAuthority> auths = new ArrayList<>();
        if(utilisateur.isAdmin()){
            auths.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        }else{
            auths.add(new SimpleGrantedAuthority("ROLE_USER"));
        }
        return new CustomUserDetails(
                utilisateur.getUtilisateurID(),
                utilisateur.getUsername(),
                utilisateur.getPassword(),
                auths
        );
    }

    public Collection<? extends GrantedAuthority> getAuthorities(){
        return authorities;
    }


    public Integer getId() {
        return id;
    }

    @Override
    public String getUsername() {
        return username;
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

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CustomUserDetails)) return false;
        CustomUserDetails that = (CustomUserDetails) o;
        return Objects.equals(id, that.id) && Objects.equals(getUsername(), that.getUsername()) && Objects.equals(getPassword(), that.getPassword()) && Objects.equals(getAuthorities(), that.getAuthorities());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, getUsername(), getPassword(), getAuthorities());
    }
}

