package com.ide.api.controller;

import com.ide.api.entities.*;
import com.ide.api.message.ResponseMessage;
import com.ide.api.repository.*;
import com.ide.api.service.*;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping(path = "documents")
public class DocumentController {

    //Injection de la couche service
    private DocumentService documentService;
    private UtilisateurService utilisateurService;
    private CategorieService categorieService;
    private AuteurService auteurService;
    private TagService tagService;
    private CategorieDocumentRepository categorieDocumentRepository;
    private UtilisateurDocumentRepository utilisateurDocumentRepository;
    private DocumentTagRepository documentTagRepository;
    private AuteurDocumentRepository auteurDocumentRepository;
    private AuteurRepository auteurRepository;
    private TagRepository tagRepository;
    private UtilisateurDocumentService utilisateurDocumentService;
    private CategorieDocumentService categorieDocumentService;

    //Le contructeur de notre classe

    public DocumentController(DocumentService documentService,
                              UtilisateurService utilisateurService,
                              CategorieService categorieService,
                              CategorieDocumentRepository categorieDocumentRepository,
                              UtilisateurDocumentRepository utilisateurDocumentRepository,
                              AuteurService auteurService,
                              TagService tagService,
                              DocumentTagRepository documentTagRepository,
                              AuteurDocumentRepository auteurDocumentRepository,
                              AuteurRepository auteurRepository,
                              TagRepository tagRepository,
                              UtilisateurDocumentService utilisateurDocumentService,
                              CategorieDocumentService categorieDocumentService) {
        this.documentService = documentService;
        this.utilisateurService = utilisateurService;
        this.categorieService = categorieService;
        this.categorieDocumentRepository = categorieDocumentRepository;
        this.utilisateurDocumentRepository = utilisateurDocumentRepository;
        this.auteurService = auteurService;
        this.tagService = tagService;
        this.documentTagRepository = documentTagRepository;
        this.auteurDocumentRepository = auteurDocumentRepository;
        this.auteurRepository = auteurRepository;
        this.tagRepository = tagRepository;
        this.utilisateurDocumentService = utilisateurDocumentService;
        this.categorieDocumentService = categorieDocumentService;
    }


    //La methode qui permet d'ajouter un document, utiliser directement coté client

    @PostMapping(value="/ajouter", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<ResponseMessage> addDocument(@RequestParam(required = false, name = "resume") String resume,
                                                       @RequestParam(required = false, name = "proprietaire") String proprietaire,
                                                       @RequestParam(required = false, name = "langue") String langue,
                                                       @RequestParam(required = true, name = "categorieID") Integer categorieID,
                                                       @RequestParam(required = true, name = "utilisateurID") Integer utilisateurID,
                                                       @RequestParam(name = "auteurID", required = false) Integer auteurID,
                                                       @RequestParam(name = "tagID", required = false) Integer tagID,
                                                       @RequestParam(value = "paysPublication", required = false) String paysPublication,
                                                       @RequestParam("file") MultipartFile file) throws IOException{

        Document document = new Document();
        String message = "";
        if(resume != null)
            document.setResume(resume);
        document.setTitre(file.getOriginalFilename());
        document.setTaille(file.getBytes());
        document.setFormat(file.getContentType());
        if(proprietaire != null)
            document.setProprietaire(proprietaire);
        if(langue != null)
            document.setLangue(langue);
        document.setNombreDeCommentaires(0);
        document.setNombreDeConsultations(0);
        document.setNombreDeTelechargements(0);
        document.setDatePublication(new Date());




        try{
            Utilisateur utilisateur = new Utilisateur();
            utilisateur = utilisateurService.findUtilisateur(utilisateurID);

            if(utilisateur.isAdmin()){
                this.documentService.addDocument(
                        document
                );
                UtilisateurDocument utilisateurDocument = new UtilisateurDocument();
                utilisateurDocument.setDocument(document);
                utilisateurDocument.setUtilisateur(utilisateur);
                utilisateurDocument.setDateCreation(new Date());
                utilisateurDocumentService.createUtilisateurDocument(utilisateurDocument);

                Categorie categorie = new Categorie();
                categorie = categorieService.findCategory(categorieID);
                CategorieDocument categorieDocument = new CategorieDocument();
                categorieDocument.setDocument(document);
                categorieDocument.setCategorie(categorie);
                categorieDocumentService.createCategorieDocument(categorieDocument);

                if(auteurID != null){
                    Optional<Auteur> auteurtest = auteurRepository.findById(auteurID);
                    if(auteurtest.isPresent()){
                        Auteur auteur = auteurService.findAuteur(auteurID);
                        AuteurDocument auteurDocument = new AuteurDocument();
                        auteurDocument.setDocument(document);
                        auteurDocument.setAuteur(auteur);
                        auteurDocument.setPaysPublication(paysPublication);
                        auteurDocument.setDateCreation(new Date());
                        auteurDocumentRepository.save(auteurDocument);
                    }
                }

                if(tagID != null){
                    Optional<Tag> tagOptional = tagRepository.findById(tagID);
                    if(tagOptional.isPresent()){
                        Tag tag = tagService.findTag(tagID);
                        DocumentTag documentTag = new DocumentTag();
                        documentTag.setDocument(document);
                        documentTag.setTag(tag);
                        documentTagRepository.save(documentTag);
                    }
                }
                message = "Succès de chargement du fichier : " + file.getOriginalFilename();
                return ResponseEntity
                        .status(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(new ResponseMessage(message));
            }else {
                message = "Utilisateur non privilégié...";
                return ResponseEntity
                        .status(HttpStatus.EXPECTATION_FAILED)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(new ResponseMessage(message));
            }


        }catch (Exception e){
            message = "Echec de chargement du fichier : " + file.getOriginalFilename() + "!";
            return ResponseEntity
                    .status(HttpStatus.EXPECTATION_FAILED)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(new ResponseMessage(message));
        }
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody List<Document> retrieveAllDocuments(){
        return documentService.findDocuments();
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Resource> recupererUnDocument(@PathVariable Integer id){
        Optional<Document> optionalDocument = documentService.findDocument(id);
        if(optionalDocument.isPresent()){
            Document document = optionalDocument.get();
            byte[] fileContent = document.getTaille();
            ByteArrayResource resource = new ByteArrayResource(fileContent);
            String contentType = determineContentType(document.getTitre());
            return ResponseEntity.ok()
                    .header("Content-Disposition","inline")
                    .header("Content-Type", contentType)
                    .body(resource);
        }else {
            return ResponseEntity.notFound().build();
        }

    }


    @GetMapping(value = "/download/{id}")
    public ResponseEntity<Resource> downloadDocument(@PathVariable Integer id,
                                                     HttpServletRequest request){
        Optional<Document> optionalDocument = documentService.findDocument(id);
        if(optionalDocument.isPresent()){
            Document document = optionalDocument.get();
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(document.getFormat()))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + document.getTitre() + "\"")
                    .body(new ByteArrayResource(document.getTaille()));
        }else{
            return ResponseEntity.notFound().build();
        }
    }

    private  String determineContentType(String fileName){
        String fileExtension  = Optional.ofNullable(fileName)
                .filter(f -> f.contains("."))
                .map(f -> f.substring(fileName.lastIndexOf(".")+1))
                .orElse("");
        switch (fileExtension.toLowerCase()){
            case "pdf":
                return "application/pdf";
            case "jpeg":
                return "image/jpeg";
            case "png":
                return "image/png";
            case "gif":
                return "image/gif";
            default:
                return "application/octet-stream";
        }
    }
}
