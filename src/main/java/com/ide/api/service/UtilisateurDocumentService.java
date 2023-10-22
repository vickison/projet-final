package com.ide.api.service;

import com.ide.api.entities.UtilisateurCategorie;
import com.ide.api.entities.UtilisateurDocument;
import com.ide.api.repository.UtilisateurDocumentRepository;
import org.springframework.stereotype.Service;

@Service
public class UtilisateurDocumentService {

    private UtilisateurDocumentRepository utilisateurDocumentRepository;

    public UtilisateurDocumentService(UtilisateurDocumentRepository utilisateurDocumentRepository) {
        this.utilisateurDocumentRepository = utilisateurDocumentRepository;
    }

    public void createUtilisateurDocument(UtilisateurDocument utilisateurDocument){
        this.utilisateurDocumentRepository.save(utilisateurDocument);
    }
}
