package com.ide.api.controller;

import com.ide.api.dto.DocumentDTO;
import com.ide.api.dto.LikeCountDTO;
import com.ide.api.dto.LikeIllustrationDTO;
import com.ide.api.entities.*;
import com.ide.api.enums.Mention;
import com.ide.api.enums.TypeFichier;
import com.ide.api.enums.TypeGestion;
import com.ide.api.message.ResponseMessage;
import com.ide.api.repository.*;
import com.ide.api.service.*;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping(path = "documents")
public class DocumentController {

    private final String basePath ="C:\\Users\\avicky\\libeil\\";
    private DocumentRepository documentRepository;
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
    private FileService fileService;
    private LikeIllustrationService likeIllustrationService;

    //Le contructeur de notre classe

    public DocumentController(DocumentService documentService,
                              DocumentRepository documentRepository,
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
                              CategorieDocumentService categorieDocumentService,
                              FileService fileService,
                              LikeIllustrationService likeIllustrationService) {
        this.documentService = documentService;
        this.documentRepository = documentRepository;
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
        this.fileService = fileService;
        this.likeIllustrationService = likeIllustrationService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(value = "/admin/ajouter",
            consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    ResponseEntity<ResponseMessage> ajouterDocument(@ModelAttribute DocumentDTO documentDTO,
                                                    @RequestParam(name = "newTitle", required = false) String newTitle,
                                                    @RequestParam(required = true, name = "categorieID") List<Integer> categorieID,
                                                    @RequestParam(name = "auteurID", required = false) List<Integer> auteurID,
                                                    @RequestParam(name = "tagID", required = false) List<Integer> tagID) throws IOException, NoSuchAlgorithmException, InvalidKeyException {
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Integer utilisateurID = userDetails.getId();
        fileService.storeFile(updateFileName(documentDTO.getFile(),newTitle),
                creerDossier(documentDTO.getFile().getOriginalFilename()),
                Base64.getEncoder().encodeToString(documentDTO.getFile().getBytes()));
        Document document = new Document();
        String msg = "";
        System.out.println("------------------------------");
        document.setTitre(updateFileName(documentDTO.getFile(),newTitle));
        document.setTaille(documentDTO.getFile().getSize());
        document.setFormat(documentDTO.getFile().getContentType());
        document.setResume(documentDTO.getResume());
        if(newTitle != null){
            switch (Objects.requireNonNull(documentDTO.getFile().getContentType())){
                case "application/pdf":
                    document.setUrl(basePath+creerDossier(documentDTO.getFile().getOriginalFilename())+"\\"+newTitle+".pdf");
                    break;
                case "video/mp4":
                    document.setUrl(basePath+creerDossier(documentDTO.getFile().getOriginalFilename())+"\\"+newTitle+".mp4");
                    break;
                case "audio/mp3":
                    document.setUrl(basePath+creerDossier(documentDTO.getFile().getOriginalFilename())+"\\"+newTitle+".mp3");
                    break;
                case "image/jpeg":
                    document.setUrl(basePath+creerDossier(documentDTO.getFile().getOriginalFilename())+"\\"+newTitle+".jpeg");
                    break;
                case "image/jpg":
                    document.setUrl(basePath+creerDossier(documentDTO.getFile().getOriginalFilename())+"\\"+newTitle+".jpg");
                    break;
                case "image/png":
                    document.setUrl(basePath+creerDossier(documentDTO.getFile().getOriginalFilename())+"\\"+newTitle+".png");
                    break;
                case "image/gif":
                    document.setUrl(basePath+creerDossier(documentDTO.getFile().getOriginalFilename())+"\\"+newTitle+".gif");
                    break;
                default:
                    document.setUrl(basePath+creerDossier(documentDTO.getFile().getOriginalFilename())+"\\"+documentDTO.getFile().getOriginalFilename());
            }
        }else{
            document.setUrl(basePath+creerDossier(documentDTO.getFile().getOriginalFilename())+"\\"+documentDTO.getFile().getOriginalFilename());
        }
        document.setLangue(documentDTO.getLangue());
        document.setTypeFichier(typeFichier(documentDTO.getFile().getOriginalFilename()));
        System.out.println("------------------------------");
        try{
            Utilisateur existingutilisateur = utilisateurService.findUtilisateur(utilisateurID);
            document.setAuteurCreationDocument(existingutilisateur.getUsername());
            if(existingutilisateur.isAdmin()){
                System.out.println(">Admin user...");
                System.out.println("Path: "+document.getUrl());
                System.out.println(document.getTaille());
                this.documentService.creerDocument(document, categorieID, tagID, auteurID);
                //this.documentRepository.save(document);
                UtilisateurDocument utilisateurDocument = new UtilisateurDocument();
                utilisateurDocument.setDocument(document);
                utilisateurDocument.setUtilisateur(existingutilisateur);
                utilisateurDocument.setTypeGestion(TypeGestion.Ajouter);
                utilisateurDocumentService.createUtilisateurDocument(utilisateurDocument);

                msg = "Succès de chargement du fichier : " + documentDTO.getFile().getOriginalFilename();
                return ResponseEntity
                        .status(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(new ResponseMessage(msg));
            }else {
                msg = "Utilisateur non privilégié...";
                return ResponseEntity
                        .status(HttpStatus.EXPECTATION_FAILED)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(new ResponseMessage(msg));
            }
        } catch (Exception e){
            msg = "Echec de chargement du fichier : " + documentDTO.getFile().getOriginalFilename() + "!";
            return ResponseEntity
                    .status(HttpStatus.EXPECTATION_FAILED)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(new ResponseMessage(msg));
        }
    }

    @GetMapping(value = "/public", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody List<Document> retrieveAllDocuments(){
        return documentService.findDocuments();
    }
    @GetMapping(value = "/public/types/{type}", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody List<Document> findDocumentsByType(@PathVariable TypeFichier type){
        return this.documentService.findDocumentsByType(type);
    }

    @GetMapping(value = "/public/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Resource> recupererUnDocument(@PathVariable Integer id) throws MalformedURLException {
        Optional<Document> optionalDocument = documentService.findDocument(id);
        if(optionalDocument.isPresent()){
            Document document = optionalDocument.get();
            Path filePath = Paths.get(document.getUrl());
            Resource resource = new UrlResource(filePath.toUri());
            String contentType = determineContentType(document.getTitre());
            return ResponseEntity.ok()
                    .header("Content-Disposition","inline")
                    .header("Content-Type", contentType)
                    .body(resource);
        }else {
            return ResponseEntity.notFound().build();
        }

    }


    @GetMapping(value = "/public/download/{id}")
    public ResponseEntity<Resource> downloadDocument(@PathVariable Integer id,
                                                     HttpServletRequest request) throws MalformedURLException {
        Optional<Document> optionalDocument = documentService.findDocument(id);
        if(optionalDocument.isPresent()){
            Document document = optionalDocument.get();
            Path filePath = Paths.get(document.getUrl());
            Resource resource = new UrlResource(filePath.toUri());
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(document.getFormat()))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + document.getTitre() + "\"")
                    .body(resource);
        }else{
            return ResponseEntity.notFound().build();
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping(value = "/admin/update/{documentID}")
    public ResponseEntity<Document> updateDocument(@PathVariable Integer documentID,
                                                   @ModelAttribute Document document) throws IOException {
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Integer adminID = userDetails.getId();
        Optional<Document> selectedDocument = this.documentService.findDocument(documentID);
        Utilisateur utilisateur = this.utilisateurService.findUtilisateur(adminID);
        if(selectedDocument.isPresent()){
            Document existingDocument = selectedDocument.get();
            existingDocument.setResume(document.getResume());
            existingDocument.setLangue(document.getLangue());
            existingDocument.setAuteurModificationDocument(utilisateur.getUsername());
            final Document updatedDocument = this.documentRepository.save(existingDocument);
            Optional<UtilisateurDocument> utilDoc = this.utilisateurDocumentService.findByDocAndUtil(updatedDocument, utilisateur);
            if(utilDoc.isPresent()){
                UtilisateurDocument utilisateurDocument= utilDoc.get();
                utilisateurDocument.setTypeGestion(TypeGestion.Modifier);
                this.utilisateurDocumentService.createUtilisateurDocument(utilisateurDocument);
                System.out.println("Inside condition");
            }else {
                UtilisateurDocument newUtilDoc = new UtilisateurDocument();
                newUtilDoc.setUtilisateurID(utilisateur);
                newUtilDoc.setDocumentID(updatedDocument);
                newUtilDoc.setTypeGestion(TypeGestion.Modifier);
                this.utilisateurDocumentService.createUtilisateurDocument(newUtilDoc);
                System.out.println("Not inside condition");
            }
            return ResponseEntity.ok(updatedDocument);
        }else{
            return ResponseEntity.notFound().build();
        }

    }
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping(value = "/admin/delete/{documentID}")
    public ResponseEntity<Document> deleteDocument(@PathVariable Integer documentID) throws IOException{
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Integer adminID = userDetails.getId();
        Optional<Document> selectedDocument = this.documentService.findDocument(documentID);
        Utilisateur utilisateur = this.utilisateurService.findUtilisateur(adminID);
        if(selectedDocument.isPresent()){
            Document existingDoc = selectedDocument.get();
            existingDoc.setSupprimerDocument(true);
            existingDoc.setAuteurModificationDocument(utilisateur.getUsername());
            final Document deleteDoc = this.documentRepository.save(existingDoc);
            Optional<UtilisateurDocument> utilDoc = this.utilisateurDocumentService.findByDocAndUtil(deleteDoc, utilisateur);
            if(utilDoc.isPresent()){
                UtilisateurDocument utilisateurDocument= utilDoc.get();
                utilisateurDocument.setTypeGestion(TypeGestion.Supprimer);
                this.utilisateurDocumentService.createUtilisateurDocument(utilisateurDocument);
                System.out.println("Inside condition");
            }else {
                UtilisateurDocument newUtilDoc = new UtilisateurDocument();
                newUtilDoc.setUtilisateurID(utilisateur);
                newUtilDoc.setDocumentID(deleteDoc);
                newUtilDoc.setTypeGestion(TypeGestion.Supprimer);
                this.utilisateurDocumentService.createUtilisateurDocument(newUtilDoc);
                System.out.println("Not inside condition");
            }
            return ResponseEntity.ok(deleteDoc);
        }else {
            return ResponseEntity.notFound().build();
        }
    }

//    @GetMapping(value = "/search/{criteres}")
//    public @ResponseBody List<Document> searchDocuments(@PathVariable String criteres){
//        return this.documentService.rechercherDocument(criteres);
//    }

    @GetMapping(value = "/public/search")
    public @ResponseBody List<Document> searchDocuments(@RequestParam String keywords){
        return this.documentService.searchDocumentByKeyWords(keywords);
    }

    @GetMapping(value = "/public/rechercher")
    public @ResponseBody List<Document> rechercherDocuments(@RequestParam String motCles){
        return this.documentService.searchDocument(motCles);
    }

    @GetMapping("/public/sorted")
    public ResponseEntity<List<Document>> orderByCritria(@RequestParam(name = "sortBy") String sortBy){
        List<Document> documentList = documentService.getDocumentsSortedBy(sortBy);
        return ResponseEntity.ok(documentList);
    }

    @PutMapping(value = "/public/{id}/like")
    public ResponseEntity<String> likeIllustration(@PathVariable Integer id,
                                                    HttpServletRequest request){
        String utilIP = this.getClientIpAddress(request);
        String likeOrUnlike = "";

        if(this.likeIllustrationService.existingLike(id, utilIP)){
            LikeIllustration likeIllustration = this.likeIllustrationService.findLikedIllus(id, utilIP);
            if(likeIllustration.getMention() == Mention.like){
                likeIllustration.setMention(Mention.unlike);
                likeOrUnlike = "Illustration non aimée...";
            }else if(likeIllustration.getMention() == Mention.unlike){
                likeIllustration.setMention(Mention.like);
                likeOrUnlike = "Illustration aimée...";
            }
            this.likeIllustrationService.createLikeIllustration(likeIllustration);
            return ResponseEntity.ok(likeOrUnlike);
        }else{
            LikeIllustration likeIllustration = new LikeIllustration();
            likeIllustration.setDocumentID(id);
            likeIllustration.setUtilisateurIP(utilIP);
            likeIllustration.setMention(Mention.like);
            this.likeIllustrationService.createLikeIllustration(likeIllustration);
            if(likeIllustration.getMention() == Mention.like){
                likeOrUnlike = "Illustration aimée...";
            }else {
                likeOrUnlike = "Illustration non aimée...";
            }
            return ResponseEntity.ok(likeOrUnlike);
        }

    }
    @GetMapping(value = "/public/{docID}/like/count")
    public @ResponseBody List<LikeCountDTO> countMentions(@PathVariable Integer docID){
        return this.likeIllustrationService.countByMention(docID);
    }

    @GetMapping(value = "/public/{docId}/liked")
    public boolean isDocumentLikedBy(@PathVariable Integer docId,
                                     HttpServletRequest request){
        String utilIP = this.getClientIpAddress(request);
        boolean likeOrUnlike = this.likeIllustrationService.existingLike(docId, utilIP);
        if(likeOrUnlike){
            LikeIllustration likeIllustration = this.likeIllustrationService.findLikedIllus(docId, utilIP);
            if(likeIllustration.getMention() == Mention.like){
                return true;
            }
            return  false;
        }
        return false;
    }


    private  String determineContentType(String fileName){
        String fileExtension  = Optional.ofNullable(fileName)
                .filter(f -> f.contains("."))
                .map(f -> f.substring(fileName.lastIndexOf(".")+1))
                .orElse("");
        switch (fileExtension.toLowerCase()){
            case "mp3":
                return "audio/mp3";
            case "mp4":
                return "video/mp4";
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

    private String creerDossier(String nomFichier){
        String fichierExtension = Optional.ofNullable(nomFichier)
                .filter(f -> f.contains("."))
                .map(f -> f.substring(nomFichier.lastIndexOf(".")+1))
                .orElse("");
        switch (fichierExtension.toLowerCase()){
            case "mp3":
                return "audios";
            case "mp4":
            case "mpeg":
            case "webm":
                return "videos";
            case "pdf":
                return "documents";
            case "jpeg":
            case "jpg":
            case "gif":
            case "png":
            case "tiff":
            case "x-icon":
                return "images";
            default:
                return "autres";

        }
    }


    private TypeFichier typeFichier(String nomFichier){
        String fichierExtension = Optional.ofNullable(nomFichier)
                .filter(f -> f.contains("."))
                .map(f -> f.substring(nomFichier.lastIndexOf(".")+1))
                .orElse("");
        switch (fichierExtension.toLowerCase()){
            case "mp3":
                return TypeFichier.Audio;
            case "mp4":
            case "mpeg":
            case "webm":
                return TypeFichier.Vidéo;
            case "jpeg":
            case "jpg":
            case "gif":
            case "png":
            case "tiff":
            case "x-icon":
                return TypeFichier.Image;
            default:
                return TypeFichier.Document;

        }
    }

    private String updateFileName(MultipartFile file,
                                  String newTitle){
        String originalFileName = file.getOriginalFilename();
        String extension = "";
        String newFileName = originalFileName;
        assert originalFileName != null;
        int extensionIndex = originalFileName.lastIndexOf('.');
        if(extensionIndex != -1){
            extension = originalFileName.substring(extensionIndex);
        }
        if(newTitle != null && !originalFileName.trim().isEmpty()){
            newFileName = newTitle + extension;
        }
        return  newFileName;
    }


    private String getClientIpAddress(HttpServletRequest request) {
        String ipAddress = request.getHeader("X-Forwarded-For");
        if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getRemoteAddr();
        }
        return ipAddress;
    }
}
