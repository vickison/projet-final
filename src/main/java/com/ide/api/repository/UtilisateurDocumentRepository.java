package com.ide.api.repository;

import com.ide.api.entities.UtilisateurDocument;
import com.ide.api.entities.UtilisateurDocumentID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UtilisateurDocumentRepository extends JpaRepository<UtilisateurDocument, UtilisateurDocumentID> {
}
