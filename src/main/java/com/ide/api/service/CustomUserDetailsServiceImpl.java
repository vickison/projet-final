package com.ide.api.service;

import com.ide.api.entities.CustomUser;
import com.ide.api.entities.Utilisateur;
import com.ide.api.repository.UtilisateurRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.security.core.GrantedAuthority;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class CustomUserDetailsServiceImpl implements UserDetailsService {

    // Assume you have a UserRepository to interact with your database
    private final UtilisateurRepository utilisateurRepository;

    public CustomUserDetailsServiceImpl(UtilisateurRepository utilisateurRepository) {
        this.utilisateurRepository = utilisateurRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Utilisateur utilisateur = utilisateurRepository.findByUsername(username);

        // You can return a UserDetails implementation, for example, org.springframework.security.core.userdetails.User
        return new CustomUser(
                utilisateur.getUsername(),
                utilisateur.getPassword(),
                getAuthorities(utilisateur.isAdmin()),
                utilisateur.isAdmin()
        );
    }

    // Helper method to convert roles to authorities
    private Collection<? extends GrantedAuthority> getAuthorities(boolean isAdmin) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));

        if (isAdmin) {
            authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        }

        return authorities;
    }
}

