package com.ide.api.service;

import com.ide.api.entities.Auteur;
import com.ide.api.entities.Utilisateur;
import com.ide.api.entities.UtilisateurAuteur;
import com.ide.api.enums.TypeGestion;
import com.ide.api.repository.AuteurRepository;
import com.ide.api.repository.UtilisateurAuteurRepository;
import com.ide.api.repository.UtilisateurRepository;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;

import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    @Transactional
    public void createAuteur(Auteur auteur, Integer idUtilisateur) {
        Logger logger = LoggerFactory.getLogger(getClass());

        try {
            if (auteur == null) {
                throw new IllegalArgumentException("L'auteur ne peut pas être nul.");
            }
            if (idUtilisateur == null) {
                throw new IllegalArgumentException("L'identifiant de l'utilisateur ne peut pas être nul.");
            }

            Auteur savedAuteur = this.auteurRepository.save(auteur);

            Utilisateur utilisateur = this.utilisateurRepository.findById(idUtilisateur)
                    .orElseThrow(() -> new EntityNotFoundException("Utilisateur avec identifiant: " + idUtilisateur + " introuvable"));

            UtilisateurAuteur utilisateurAuteur = new UtilisateurAuteur();
            utilisateurAuteur.setAuteur(savedAuteur);
            utilisateurAuteur.setUtilisateur(utilisateur);
            utilisateurAuteur.setTypeGestion(TypeGestion.Ajouter);
            this.utilisateurAuteurRepository.save(utilisateurAuteur);

            logger.info("Auteur créé et associé avec succès : {}", savedAuteur);

        } catch (Exception e) {
            logger.error("Erreur lors de la création de l'auteur avec ID utilisateur : {}", idUtilisateur, e);
            throw new RuntimeException("Erreur lors de la création de l'auteur avec ID utilisateur : " + idUtilisateur, e);
        }
    }


    public List<Auteur> findAuteurs() {
        Logger logger = LoggerFactory.getLogger(getClass());

        try {
            List<Auteur> auteurs = this.auteurRepository.findAll();
            logger.info("Nombre d'auteurs trouvés : {}", auteurs.size());
            return auteurs;
        } catch (Exception e) {
            logger.error("Erreur lors de la récupération des auteurs", e);
            throw new RuntimeException("Erreur lors de la récupération des auteurs", e);
        }
    }


    public Auteur findAuteur(Integer id) {
        Logger logger = LoggerFactory.getLogger(getClass());

        if (id == null || id <= 0) {
            logger.warn("ID invalide fourni pour rechercher un auteur: {}", id);
            throw new IllegalArgumentException("ID invalide pour la recherche d'un auteur");
        }

        try {
            return this.auteurRepository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("Auteur avec identifiant: " + id + " introuvable"));
        } catch (EntityNotFoundException e) {
            logger.error("Erreur lors de la recherche de l'auteur avec l'identifiant: {}", id, e);
            throw e;
        }
    }


    public List<Auteur> findAuteursByUtilisateurId(Utilisateur utilisateur) {
        Logger logger = LoggerFactory.getLogger(getClass());
        if (utilisateur == null || utilisateur.getUtilisateurID() == null) {
            logger.warn("Utilisateur nul ou identifiant utilisateur nul fourni pour rechercher les auteurs.");
            throw new IllegalArgumentException("Utilisateur et son ID doivent être non null pour rechercher des auteurs.");
        }

        try {
            return this.auteurRepository.findByUtilisateurAuteursUtilisateurID(utilisateur);
        } catch (DataAccessException e) {
            logger.error("Erreur d'accès aux données lors de la recherche des auteurs pour l'utilisateur avec l'identifiant: {}", utilisateur.getUtilisateurID(), e);
            throw new RuntimeException("Erreur lors de la recherche des auteurs pour l'utilisateur avec l'identifiant: " + utilisateur.getUtilisateurID(), e);
        }
    }
}
