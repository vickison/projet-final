package com.ide.api.controller;

import com.ide.api.entities.Document;
import com.ide.api.service.DocumentService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping(path = "documents")
public class DocumentDownloadController {

    private DocumentService documentService;

    public DocumentDownloadController(DocumentService documentService) {
        this.documentService = documentService;
    }

    @GetMapping(value = "/download/{id}")
    public ResponseEntity<Resource> downloadDocument(@PathVariable Integer id, HttpServletRequest request){
        Document document = documentService.retrieveDocument(id);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(document.getType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + document.getTitre() + "\"")
                .body(new ByteArrayResource(document.getData()));
    }
}
