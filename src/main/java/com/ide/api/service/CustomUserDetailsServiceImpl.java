package com.ide.api.service;

import com.ide.api.entities.CustomUserDetails;
import com.ide.api.entities.Utilisateur;
import com.ide.api.repository.UtilisateurRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class CustomUserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    private static final Logger logger = LoggerFactory.getLogger(CustomUserDetailsServiceImpl.class);


    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        logger.info("Inside loadUserByUsername... ");
        Utilisateur utilisateur = this.utilisateurRepository.findByUsername(username);
        if (utilisateur == null) {
            logger.error("Username not found: " + username);
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
        logger.info("User found Successfully...");
        logger.info(utilisateur.getUsername());
        return CustomUserDetails.build(utilisateur);
    }
}

