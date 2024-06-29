package com.ide.api.service;

import com.ide.api.entities.AdminUtilisateur;
import com.ide.api.entities.AuteurDocument;
import com.ide.api.entities.Utilisateur;
import com.ide.api.repository.AdminUtilisateurRepository;
import com.ide.api.repository.AuteurDocumentRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuteurDocumentService {
    private AuteurDocumentRepository auteurDocumentRepository;

    public AuteurDocumentService(AuteurDocumentRepository auteurDocumentRepository) {
        this.auteurDocumentRepository = auteurDocumentRepository;
    }
    public void createAuteurDocument(AuteurDocument auteurDocument){
        this.auteurDocumentRepository.save(auteurDocument);
    }

}
