package com.ide.api.repository;

import com.ide.api.dto.CategorieDocumentDTO;
import com.ide.api.entities.Categorie;
import com.ide.api.entities.Document;
import com.ide.api.entities.Utilisateur;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CategorieRepository extends JpaRepository<Categorie, Integer> {
    List<Categorie> findByUtilisateurCategoriesUtilisateurID(Utilisateur utilisateur);
    Optional<Categorie> findByCategorieID(Integer categorieId);

    @Query("SELECT c FROM Categorie c LEFT JOIN FETCH c.categorieDocuments cd LEFT JOIN FETCH cd.documentID")
    List<Categorie> findAllCategoriesWithDocuments();
}
