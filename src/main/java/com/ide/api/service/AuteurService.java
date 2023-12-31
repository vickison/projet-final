package com.ide.api.service;

import com.ide.api.entities.Auteur;
import com.ide.api.entities.Utilisateur;
import com.ide.api.repository.AuteurRepository;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
public class AuteurService {
    private AuteurRepository auteurRepository;

    public AuteurService(AuteurRepository auteurRepository) {
        this.auteurRepository = auteurRepository;
    }

    public void createAuteur(Auteur auteur){
        this.auteurRepository.save(auteur);
    }

    public List<Auteur> findAuteurs(){
        return this.auteurRepository.findAll();
    }

    public Auteur findAuteur(Integer id){
        return this.auteurRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Auteur not found with id: " + id));
    }

    public List<Auteur> findAuteursByUtilisateurId(Utilisateur utilisateur){
        return this.auteurRepository.findByUtilisateurAuteursUtilisateurID(utilisateur);
    }
}
