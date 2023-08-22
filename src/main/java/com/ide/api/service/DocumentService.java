package com.ide.api.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ide.api.entities.Categorie;
import com.ide.api.entities.Document;
import com.ide.api.message.ResponseDocument;
import com.ide.api.repository.DocumentRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Stream;


@Service
public class DocumentService {

    //Appel de notre repository(Injection de dépendance)
    private DocumentRepository documentRepository;


    //Le constructeur de notre classe
    public DocumentService(DocumentRepository documentRepository) {
        this.documentRepository = documentRepository;
    }

    /*
        La methode qui permet de sauvegarder des documents dans la base de données documents
        Cette methode prend param:
        Une variable de type chaine de caractere poour la description du doc,
         "     "         "   Multipart qui permet d'ajouter un fichier, on extrait les infos de ce fichier,
         "     "         "   Categorie qui permet de référencer la categorie du document
     */
    public Document ajouterDocument(String desc, MultipartFile file, Categorie categorie) throws IOException {
        Document document = new Document();
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        document.setTitre(fileName);
        document.setType(file.getContentType());
        document.setData(file.getBytes());
        document.setDate_creation(Calendar.getInstance().toInstant());
        document.setDescription(desc);
        document.setCategorie(categorie);
        return this.documentRepository.save(document);
    }

    public Stream<Document> recupererToutLesDocuments(){
        return documentRepository.findAll().stream();
    }

    public Document recupererUnDocument(Integer id){
        return this.documentRepository.findById(id).get();
    }
}
