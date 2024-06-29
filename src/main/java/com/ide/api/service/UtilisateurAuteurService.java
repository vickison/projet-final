package com.ide.api.service;

import com.ide.api.entities.Auteur;
import com.ide.api.entities.Utilisateur;
import com.ide.api.entities.UtilisateurAuteur;
import com.ide.api.enums.TypeGestion;
import com.ide.api.repository.UtilisateurAuteurRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UtilisateurAuteurService {
    private UtilisateurAuteurRepository utilisateurAuteurRepository;

    public UtilisateurAuteurService(UtilisateurAuteurRepository utilisateurAuteurRepository) {
        this.utilisateurAuteurRepository = utilisateurAuteurRepository;
    }

    public void createUtilisateurAuteur(UtilisateurAuteur utilisateurAuteur){
        this.utilisateurAuteurRepository.save(utilisateurAuteur);
    }

    public UtilisateurAuteur findByUtilAuteurByAuteurID(Auteur auteur){
        UtilisateurAuteur utilisateurAuteur = new UtilisateurAuteur();
        List<UtilisateurAuteur> utilisateurAuteurs = this.utilisateurAuteurRepository.findByAuteurID(auteur);
        for(UtilisateurAuteur ua: utilisateurAuteurs){
            if(ua.getTypeGestion() == TypeGestion.Ajouter){
                utilisateurAuteur = ua;
            }
        }
        return utilisateurAuteur;
    }



    public Optional<UtilisateurAuteur> findByAuteurAndUtil(Auteur auteur, Utilisateur utilisateur){
        return this.utilisateurAuteurRepository.findByAuteurIDAndUtilisateurID(auteur, utilisateur);
    }
}
