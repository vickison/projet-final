package com.ide.api.controller;

import com.ide.api.entities.Categorie;
import com.ide.api.entities.Document;
import com.ide.api.message.ResponseDocument;
import com.ide.api.message.ResponseMessage;
import com.ide.api.service.DocumentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

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
    public ResponseEntity<ResponseMessage> ajouterDocument(@RequestParam String description, @RequestParam Categorie categorie, @RequestParam("file") MultipartFile file) throws IOException{
        String message = "";
        try{
            this.documentService.ajouterDocument(description, file, categorie);
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
        List<ResponseDocument> documents = documentService.recupererToutLesDocuments().map(doc->{
            String docDownloadUri = ServletUriComponentsBuilder
                    .fromCurrentContextPath()
                    .path("/documents/")
                    .path(doc.getFile_id().toString())
                    .toUriString();
            return new ResponseDocument(
                    doc.getFile_id(),
                    doc.getTitre(),
                    doc.getDescription(),
                    doc.getDate_creation(),
                    doc.getType(),
                    doc.getData().length,
                    doc.getCategorie()
            );
        }).collect(Collectors.toList());
        return ResponseEntity.status(HttpStatus.OK).body(documents);
    }
}
