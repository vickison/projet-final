package com.ide.api.repository;

import com.ide.api.entities.Auteur;
import com.ide.api.entities.Utilisateur;
import com.ide.api.entities.UtilisateurAuteur;
import com.ide.api.entities.UtilisateurAuteurID;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UtilisateurAuteurRepository extends JpaRepository<UtilisateurAuteur, UtilisateurAuteurID> {
    UtilisateurAuteur findByUtilisateurID(Utilisateur utilisateur);
    List<UtilisateurAuteur> findByAuteurID(Auteur auteur);
    Optional<UtilisateurAuteur> findByAuteurIDAndUtilisateurID(Auteur auteur, Utilisateur utilisateur);
}
