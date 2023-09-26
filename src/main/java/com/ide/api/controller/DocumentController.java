package com.ide.api.controller;

import com.ide.api.entities.Auteur;
import com.ide.api.entities.Categorie;
import com.ide.api.entities.Document;
import com.ide.api.message.ResponseDocument;
import com.ide.api.message.ResponseMessage;
import com.ide.api.service.DocumentService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping(path = "documents")
public class DocumentController {

    //Injection de la couche service
    private DocumentService documentService;

    //Le contructeur de notre classe
    public DocumentController(DocumentService documentService) {
        this.documentService = documentService;
    }

    //La methode qui permet d'ajouter un document, utiliser directement coté client

    @PostMapping(value="/ajouter", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<ResponseMessage> addDocument(@RequestParam String description, @RequestParam Set<Auteur> auteur, @RequestParam Set<Categorie> categorie, @RequestParam("file") MultipartFile file) throws IOException{

        String message = "";
        try{
            this.documentService.addDocument(description, auteur, file, categorie);
            message = "Uploaded the file successfully: " + file.getOriginalFilename();
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(new ResponseMessage(message));
        }catch (Exception e){
            message = "Could not upload the file: " + file.getOriginalFilename() + "!";
            return ResponseEntity
                    .status(HttpStatus.EXPECTATION_FAILED)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(new ResponseMessage(message));
        }
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ResponseDocument>> recupererToutLesDocuments(){
        List<ResponseDocument> documents = documentService.retrieveDocuments().map(doc->{
            String docDownloadUri = ServletUriComponentsBuilder
                    .fromCurrentContextPath()
                    .path("/documents/")
                    .path(doc.getDocumentId().toString())
                    .toUriString();
            return new ResponseDocument(
                    doc.getDocumentId(),
                    doc.getTitre(),
                    doc.getDescription(),
                    doc.getCreation(),
                    doc.getType(),
                    doc.getData().length,
                    doc.getCategories(),
                    docDownloadUri,
                    doc.getAuteurs()
            );
        }).collect(Collectors.toList());
        return ResponseEntity.status(HttpStatus.OK).body(documents);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Document> recupererUnDocument(@PathVariable Integer id){
        Document document = documentService.retrieveDocument(id);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment, filename=\""+document.getTitre() + "\"")
                .body(document);
    }
}
