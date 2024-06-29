package com.ide.api.repository;

import com.ide.api.entities.Categorie;
import com.ide.api.entities.Utilisateur;
import com.ide.api.entities.UtilisateurCategorie;
import com.ide.api.entities.UtilisateurCategorieID;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UtilisateurCategorieRepository extends JpaRepository<UtilisateurCategorie, UtilisateurCategorieID> {
    UtilisateurCategorie findByUtilisateurID(Utilisateur utilisateur);
    List<UtilisateurCategorie> findByCategorieID(Categorie categorie);
    Optional<UtilisateurCategorie> findByUtilisateurIDAndCategorieID(Utilisateur utilisateurID, Categorie categorieID);
}
