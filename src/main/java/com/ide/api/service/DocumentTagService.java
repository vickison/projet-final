package com.ide.api.service;

import com.ide.api.entities.DocumentTag;
import com.ide.api.repository.DocumentTagRepository;
import org.springframework.stereotype.Service;

@Service
public class DocumentTagService {
    private DocumentTagRepository documentTagRepository;

    public DocumentTagService(DocumentTagRepository documentTagRepository) {
        this.documentTagRepository = documentTagRepository;
    }

    public void createDocumentTag(DocumentTag documentTag){
        this.documentTagRepository.save(documentTag);
    }
}
