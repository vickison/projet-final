package com.ide.api.repository;

import com.ide.api.entities.DocumentTag;
import com.ide.api.entities.DocumentTagID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DocumentTagRepository extends JpaRepository<DocumentTag, DocumentTagID> {
}
