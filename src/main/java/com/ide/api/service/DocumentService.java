package com.ide.api.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ide.api.dto.DocumentDTO;
import com.ide.api.entities.*;
import com.ide.api.exception.FileNotFoundException;
import com.ide.api.message.ResponseDocument;
import com.ide.api.repository.DocumentRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.stream.Stream;


@Service
public class DocumentService {

    //Appel de notre repository(Injection de dépendance)
    private DocumentRepository documentRepository;


    //Le constructeur de notre classe
    public DocumentService(DocumentRepository documentRepository) {
        this.documentRepository = documentRepository;
    }


    public void addDocument(Document document) throws IOException {
         this.documentRepository.save(document);
    }

    public List<Document> findDocuments(){
        return documentRepository.findAll();
    }

    public Optional<Document> findDocument(Integer id){
        return this.documentRepository.findById(id);
    }
    public List<Document> findDocumentsByCategoryId(Categorie categorie){
        return this.documentRepository.findByCategorieDocumentsCategorieID(categorie);
    }

    public List<Document> findDocumentsByUtilisateurId(Utilisateur utilisateur){
        return this.documentRepository.findByUtilisateurDocumentsUtilisateurID(utilisateur);
    }

    public List<Document> findDocumentsByAuteurId(Auteur auteur){
        return this.documentRepository.findByAuteurDocumentsAuteurID(auteur);
    }

    public List<Document> findDocumentsByTagId(Tag tag){
        return this.documentRepository.findByDocumentTagsDocumentID(tag);
    }
}
