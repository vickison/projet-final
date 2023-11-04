package com.ide.api.repository;

import com.ide.api.entities.Auteur;
import com.ide.api.entities.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AuteurRepository extends JpaRepository<Auteur, Integer> {
    List<Auteur> findByUtilisateurAuteursUtilisateurID(Utilisateur utilisateur);
}
