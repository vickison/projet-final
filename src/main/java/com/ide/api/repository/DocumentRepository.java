package com.ide.api.repository;

import com.ide.api.entities.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DocumentRepository extends JpaRepository<Document, Integer> {
    List<Document> findByCategorieDocumentsCategorieID(Categorie categorie);
    List<Document> findByUtilisateurDocumentsUtilisateurID(Utilisateur utilisateur);
    List<Document> findByAuteurDocumentsAuteurID(Auteur auteur);
    List<Document> findByDocumentTagsDocumentID(Tag tag);
}
