package com.ide.api.repository;

import com.ide.api.entities.Document;
import com.ide.api.entities.Utilisateur;
import com.ide.api.entities.UtilisateurDocument;
import com.ide.api.entities.UtilisateurDocumentID;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UtilisateurDocumentRepository extends JpaRepository<UtilisateurDocument, UtilisateurDocumentID> {
    UtilisateurDocument findByUtilisateurID(Utilisateur utilisateur);
    List<UtilisateurDocument> findByDocumentID(Document document);
}
