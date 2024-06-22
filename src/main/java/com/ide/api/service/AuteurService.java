package com.ide.api.service;

import com.ide.api.entities.Auteur;
import com.ide.api.entities.Utilisateur;
import com.ide.api.entities.UtilisateurAuteur;
import com.ide.api.enums.TypeGestion;
import com.ide.api.repository.AuteurRepository;
import com.ide.api.repository.UtilisateurAuteurRepository;
import com.ide.api.repository.UtilisateurRepository;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
public class AuteurService {
    private AuteurRepository auteurRepository;
    private UtilisateurRepository utilisateurRepository;
    private UtilisateurAuteurRepository utilisateurAuteurRepository;

    public AuteurService(AuteurRepository auteurRepository,
                         UtilisateurRepository utilisateurRepository,
                         UtilisateurAuteurRepository utilisateurAuteurRepository) {
        this.auteurRepository = auteurRepository;
        this.utilisateurRepository = utilisateurRepository;
        this.utilisateurAuteurRepository = utilisateurAuteurRepository;
    }

    public void createAuteur(Auteur auteur,
                             Integer idUtilisateur){
        Auteur savedAuteur = this.auteurRepository.save(auteur);
        UtilisateurAuteur utilisateurAuteur = new UtilisateurAuteur();
        Utilisateur utilisateur = this.utilisateurRepository.findById(idUtilisateur)
                .orElseThrow(() -> new EntityNotFoundException("Utilisateur avec identifiant: " + idUtilisateur + " introuvable"));

        utilisateurAuteur.setAuteur(savedAuteur);
        utilisateurAuteur.setUtilisateur(utilisateur);
        utilisateurAuteur.setTypeGestion(TypeGestion.Ajouter);
        this.utilisateurAuteurRepository.save(utilisateurAuteur);
    }

    public List<Auteur> findAuteurs(){
        return this.auteurRepository.findAll();
    }

    public Auteur findAuteur(Integer id){
        return this.auteurRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Auteur avec identifiant: " + id + " introuvable"));
    }

    public List<Auteur> findAuteursByUtilisateurId(Utilisateur utilisateur){
        return this.auteurRepository.findByUtilisateurAuteursUtilisateurID(utilisateur);
    }
}
