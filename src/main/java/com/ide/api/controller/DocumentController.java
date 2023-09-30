package com.ide.api.controller;

import com.ide.api.entities.*;
import com.ide.api.message.ResponseDocument;
import com.ide.api.message.ResponseMessage;
import com.ide.api.repository.ContenuParCategorieRepository;
import com.ide.api.repository.GestionContenusRepository;
import com.ide.api.service.AdminService;
import com.ide.api.service.CategorieService;
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
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping(path = "documents")
public class DocumentController {

    //Injection de la couche service
    private DocumentService documentService;
    private AdminService adminService;
    private CategorieService categorieService;
    private ContenuParCategorieRepository contenuParCategorieRepository;
    private GestionContenusRepository gestionContenusRepository;

    //Le contructeur de notre classe

    public DocumentController(DocumentService documentService,
                              AdminService adminService,
                              CategorieService categorieService,
                              ContenuParCategorieRepository contenuParCategorieRepository,
                              GestionContenusRepository gestionContenusRepository) {
        this.documentService = documentService;
        this.adminService = adminService;
        this.categorieService = categorieService;
        this.contenuParCategorieRepository = contenuParCategorieRepository;
        this.gestionContenusRepository = gestionContenusRepository;
    }


    //La methode qui permet d'ajouter un document, utiliser directement coté client

    @PostMapping(value="/ajouter", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<ResponseMessage> addDocument(@RequestParam  String resume,
                                                       @RequestParam int idCategorie,
                                                       @RequestParam int idAdmin,
                                                       @RequestParam("file") MultipartFile file) throws IOException{

        Document document = new Document();
        String message = "";
        document.setResume(resume);
        document.setTitre(file.getOriginalFilename());
        document.setTaille(file.getBytes());
        document.setFormatDocument(file.getContentType());
        Admin admin = new Admin();
        admin = adminService.findAdmin(idAdmin);
        Categorie categorie = new Categorie();
        categorie = categorieService.findCategory(idCategorie);
        ContenuParCategories cc = new ContenuParCategories();
        GestionContenus gc = new GestionContenus();
        cc.setCategorie(categorie);
        cc.setDocument(document);

        gc.setAdmin(admin);
        gc.setDocument(document);

        gc.setDateC(new Date());

        contenuParCategorieRepository.save(cc);
        gestionContenusRepository.save(gc);
        try{
            this.documentService.addDocument(
                    document
            );
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
    public ResponseEntity<List<ResponseDocument>> retrieveAllDocuments(){
        List<ResponseDocument> documents = documentService.retrieveAllDocuments().map(doc->{
            String docDownloadUri = ServletUriComponentsBuilder
                    .fromCurrentContextPath()
                    .path("/documents/")
                    .path(doc.getIdDocument().toString())
                    .toUriString();
            return new ResponseDocument(
                    doc.getIdDocument(),
                    doc.getTitre(),
                    doc.getResume(),
                    doc.getDateEnregistrement(),
                    doc.getFormatDocument(),
                    doc.getTaille().length,
                    docDownloadUri,
                    doc.getNombreDeTelechargements(),
                    doc.getNombreDeConsultations(),
                    doc.getProprietaire(),
                    doc.getLangue(),
                    doc.getNote(),
                    doc.getNombreCommentaires()
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
