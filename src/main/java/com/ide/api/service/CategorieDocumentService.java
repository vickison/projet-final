package com.ide.api.service;

import com.ide.api.dto.CategorieDocumentDTO;
import com.ide.api.entities.CategorieDocument;
import com.ide.api.entities.UtilisateurDocument;
import com.ide.api.repository.CategorieDocumentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategorieDocumentService {
    private CategorieDocumentRepository categorieDocumentRepository;

    public CategorieDocumentService(CategorieDocumentRepository categorieDocumentRepository) {
        this.categorieDocumentRepository = categorieDocumentRepository;
    }

    public void createCategorieDocument(CategorieDocument categorieDocument){
        this.categorieDocumentRepository.save(categorieDocument);
    }

    public List<CategorieDocumentDTO> getCategorieDocuments(){
        return categorieDocumentRepository.findCategoriesAndDocuments();
    }
}
