package com.ide.api.service;

import com.ide.api.entities.UtilisateurAuteur;
import com.ide.api.repository.UtilisateurAuteurRepository;
import org.springframework.stereotype.Service;

@Service
public class UtilisateurAuteurService {
    private UtilisateurAuteurRepository utilisateurAuteurRepository;

    public UtilisateurAuteurService(UtilisateurAuteurRepository utilisateurAuteurRepository) {
        this.utilisateurAuteurRepository = utilisateurAuteurRepository;
    }

    public void createUtilisateurAuteur(UtilisateurAuteur utilisateurAuteur){
        this.utilisateurAuteurRepository.save(utilisateurAuteur);
    }
}
