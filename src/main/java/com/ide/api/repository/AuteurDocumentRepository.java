package com.ide.api.repository;

import com.ide.api.entities.AuteurDocument;
import com.ide.api.entities.AuteurDocumentID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuteurDocumentRepository extends JpaRepository<AuteurDocument, AuteurDocumentID> {
}
