package com.ide.api.repository;

import com.ide.api.entities.Document;
import com.ide.api.entities.Utilisateur;
import com.ide.api.entities.UtilisateurDocument;
import com.ide.api.entities.UtilisateurDocumentID;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UtilisateurDocumentRepository extends JpaRepository<UtilisateurDocument, UtilisateurDocumentID> {
    UtilisateurDocument findByUtilisateurID(Utilisateur utilisateur);
    List<UtilisateurDocument> findByDocumentID(Document document);
    Optional<UtilisateurDocument> findByDocumentIDAndUtilisateurID(Document document, Utilisateur utilisateur);
}
