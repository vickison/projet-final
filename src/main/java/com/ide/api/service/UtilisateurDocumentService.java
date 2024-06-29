package com.ide.api.service;

import com.ide.api.entities.*;
import com.ide.api.enums.TypeGestion;
import com.ide.api.repository.UtilisateurDocumentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UtilisateurDocumentService {

    private UtilisateurDocumentRepository utilisateurDocumentRepository;

    public UtilisateurDocumentService(UtilisateurDocumentRepository utilisateurDocumentRepository) {
        this.utilisateurDocumentRepository = utilisateurDocumentRepository;
    }

    public void createUtilisateurDocument(UtilisateurDocument utilisateurDocument){
        this.utilisateurDocumentRepository.save(utilisateurDocument);
    }

    public UtilisateurDocument findUtilDocumentByDocumentID(Document document){
        UtilisateurDocument utilisateurDocument = new UtilisateurDocument();
        List<UtilisateurDocument> utilisateurDocuments =  this.utilisateurDocumentRepository.findByDocumentID(document);
        for(UtilisateurDocument ud: utilisateurDocuments){
            if(ud.getTypeGestion() == TypeGestion.Ajouter){
                utilisateurDocument = ud;
            }
        }
        return utilisateurDocument;
    }


    public Optional<UtilisateurDocument> findByDocAndUtil(Document document, Utilisateur utilisateur){
        return this.utilisateurDocumentRepository.findByDocumentIDAndUtilisateurID(document, utilisateur);
    }
}
