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


@Service
public class CustomUserDetailsService {

//    @Autowired
//    private UtilisateurRepository utilisateurRepository;
//
//    private static final Logger logger = LoggerFactory.getLogger(CustomUserDetailsService.class);
//
//
//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        logger.info("Inside loadUserByUsername... ");
//        Utilisateur utilisateur = this.utilisateurRepository.findByUsername(username);
//        if (utilisateur == null) {
//            logger.error("Username not found: " + username);
//            throw new UsernameNotFoundException("User not found with username: " + username);
//        }
//        logger.info("User found Successfully...");
//        logger.info(utilisateur.getUsername());
//        return new CustomUserDetails(utilisateur);
//    }
}
