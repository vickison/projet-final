package com.ide.api.repository;

import com.ide.api.entities.CategorieDocument;
import com.ide.api.entities.CategorieDocumentID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategorieDocumentRepository extends JpaRepository<CategorieDocument, CategorieDocumentID> {
}
