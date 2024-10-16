package com.ide.api.controller;

import com.ide.api.configurations.FilePaths;
import com.ide.api.dto.DocumentDTO;
import com.ide.api.dto.LikeCountDTO;
import com.ide.api.dto.LikeIllustrationDTO;
import com.ide.api.entities.*;
import com.ide.api.enums.Mention;
import com.ide.api.enums.TypeFichier;
import com.ide.api.enums.TypeGestion;
import com.ide.api.message.DocumentCreationResponse;
import com.ide.api.message.ResponseMessage;
import com.ide.api.repository.*;
import com.ide.api.service.*;
import org.apache.commons.io.FilenameUtils;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachePut;
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
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping(path = "documents")
public class DocumentController {



    private static final Map<String, String> MIME_TO_EXTENSION_MAP = new HashMap<>();

    static {
        MIME_TO_EXTENSION_MAP.put("application/pdf", ".pdf");
        MIME_TO_EXTENSION_MAP.put("video/mp4", ".mp4");
        MIME_TO_EXTENSION_MAP.put("audio/mp3", ".mp3");
        MIME_TO_EXTENSION_MAP.put("audio/mpeg", ".mp3");
        MIME_TO_EXTENSION_MAP.put("image/jpeg", ".jpeg");
        MIME_TO_EXTENSION_MAP.put("image/jpg", ".jpg");
        MIME_TO_EXTENSION_MAP.put("image/png", ".png");
        MIME_TO_EXTENSION_MAP.put("image/gif", ".gif");
        MIME_TO_EXTENSION_MAP.put("image/tiff", ".tiff");
    }


    String basePath = FilePaths.BASE_PATH;
    Path thumbnailLocation = FilePaths.THUMBNAIL_LOCATION;
    String slash_vs_antislash = FilePaths.SLASH_VS_ANTI_SLASH;

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
    private ThumbnailService thumbnailService;
    private CacheManager cacheManager;

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
                              LikeIllustrationService likeIllustrationService,
                              ThumbnailService thumbnailService,
                              CacheManager cacheManager
                              ) {
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
        this.thumbnailService = thumbnailService;
        this.cacheManager = cacheManager;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(value = "/admin/ajouter",
            consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<DocumentCreationResponse> ajouterDocument(
            @ModelAttribute DocumentDTO documentDTO,
            @RequestParam(name = "newTitle", required = false) String newTitle,
            @RequestParam(name = "categorieID", required = true) List<Integer> categorieID,
            @RequestParam(name = "auteurID", required = false) List<Integer> auteurID,
            @RequestParam(name = "tagID", required = false) List<Integer> tagID) throws IOException {

        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Integer utilisateurID = userDetails.getId();

        MultipartFile file = documentDTO.getFile();
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body(new DocumentCreationResponse("Le fichier ne peut pas être vide."));
        }


        String originalFilename = file.getOriginalFilename();
        String baseDirectory = creerDossier(originalFilename);
        String filePath = createFileUrl(file, newTitle, baseDirectory);


        fileService.storeFile(updateFileName(file, newTitle), baseDirectory, Base64.getEncoder().encodeToString(file.getBytes()));


        Document document = new Document();
        document.setTitre(updateFileName(file, newTitle));
        document.setTaille(file.getSize());
        document.setFormat(file.getContentType());
        document.setResume(documentDTO.getResume());
        document.setUrl(filePath);
        document.setLangue(documentDTO.getLangue());
        document.setTypeFichier(typeFichier(originalFilename));

        try {
            Utilisateur utilisateur = utilisateurService.findUtilisateur(utilisateurID);
            document.setAuteurCreationDocument(utilisateur.getUsername());

            if (utilisateur.isAdmin()) {
                DocumentCreationResponse response = documentService.creerDocument(document, categorieID, tagID, auteurID);

                UtilisateurDocument utilisateurDocument = new UtilisateurDocument();
                utilisateurDocument.setDocument(document);
                utilisateurDocument.setUtilisateur(utilisateur);
                utilisateurDocument.setTypeGestion(TypeGestion.Ajouter);
                utilisateurDocumentService.createUtilisateurDocument(utilisateurDocument);

                return ResponseEntity.ok(response);
            } else {
                // Répondre si l'utilisateur n'est pas un admin
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new DocumentCreationResponse("Utilisateur non privilégié..."));
            }
        } catch (Exception e) {
            // Répondre en cas d'échec
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new DocumentCreationResponse("Échec de chargement du fichier : " + originalFilename + " !"));
        }
    }

    // Crée une URL de fichier en fonction du type MIME
//    private String createFileUrl(MultipartFile file, String newTitle, String baseDirectory) {
//        String contentType = file.getContentType();
//        String extension = MIME_TO_EXTENSION_MAP.getOrDefault(contentType, getDefaultExtension(file));
//        String fileName = (newTitle != null ? newTitle : file.getOriginalFilename()) + extension;
//        return basePath + baseDirectory + "\\" + fileName;
//    }
    private String createFileUrl(MultipartFile file, String newTitle, String baseDirectory) {
        String contentType = file.getContentType();
        String extension = MIME_TO_EXTENSION_MAP.getOrDefault(contentType, getDefaultExtension(file));
        String fileName = (newTitle != null ? newTitle + extension : file.getOriginalFilename());
        String filePath = Paths.get(baseDirectory, fileName).toString();
        return Paths.get(basePath, filePath).toString();
    }

    // Obtenir l'extension par défaut si le type MIME n'est pas trouvé
//    private String getDefaultExtension(MultipartFile file) {
//        String[] parts = file.getOriginalFilename().split("\\.");
//        return parts.length > 1 ? "." + parts[1] : "";
//
//    }

//    private String getDefaultExtension(MultipartFile file) {
//        // Obtenez l'extension du fichier en utilisant FilenameUtils
//        String extension = FilenameUtils.getExtension(file.getOriginalFilename());
//        return extension.isEmpty() ? "" : "." + extension;
//    }

    private String getDefaultExtension(MultipartFile file) {
        String filename = file.getOriginalFilename();
        if (filename == null || filename.isEmpty()) {
            return ""; // Pas d'extension si le nom du fichier est vide
        }

        // Utilisation de String.lastIndexOf pour trouver le dernier point dans le nom du fichier
        int dotIndex = filename.lastIndexOf('.');
        if (dotIndex == -1 || dotIndex == filename.length() - 1) {
            return ""; // Pas d'extension si le point est absent ou est le dernier caractère
        }

        return filename.substring(dotIndex);
    }

    @GetMapping(value = "/public", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody List<Document> retrieveAllDocuments(){
        return documentService.findDocuments();
    }
    @GetMapping(value = "/public/types/{type}", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody List<Document> findDocumentsByType(@PathVariable TypeFichier type){
        return this.documentService.findDocumentsByType(type);
    }


//    private String determineContentType(String filename) {
//        String extension = getFileExtension(filename);
//        for (Map.Entry<String, String> entry : MIME_TO_EXTENSION_MAP.entrySet()) {
//            if (entry.getValue().equalsIgnoreCase(extension)) {
//                return entry.getKey();
//            }
//        }
//        return MediaType.APPLICATION_OCTET_STREAM_VALUE; // Default MIME type
//    }

    private String getFileExtension(String filename) {
        int lastIndexOfDot = filename.lastIndexOf('.');
        return (lastIndexOfDot == -1) ? "" : filename.substring(lastIndexOfDot + 1);
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

    @GetMapping(value = "/public/document/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Document> recupererUnDocumentParId(@PathVariable Integer id) {
        Optional<Document> optionalDocument = documentService.findDocument(id);
        if (optionalDocument.isPresent()) {
            Document document = optionalDocument.get();
            return ResponseEntity.ok(document); // Return the Document as JSON
        } else {
            return ResponseEntity.notFound().build(); // Return 404 if not found
        }
    }


//    @GetMapping(value = "/public/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity<StreamingResponseBody> recupererUnDocument(@PathVariable Integer id) {
//        Optional<Document> optionalDocument = documentService.findDocument(id);
//        if (optionalDocument.isPresent()) {
//            Document document = optionalDocument.get();
//            Path filePath = Paths.get(document.getUrl());
//            String contentType = determineContentType(document.getTitre());
//            try {
//                InputStream inputStream = Files.newInputStream(filePath);
//                StreamingResponseBody responseBody = outputStream -> {
//                    byte[] buffer = new byte[8192]; // Taille du tampon
//                    int bytesRead;
//                    while ((bytesRead = inputStream.read(buffer)) != -1) {
//                        outputStream.write(buffer, 0, bytesRead);
//                    }
//                    inputStream.close();
//                };
//
//                HttpHeaders headers = new HttpHeaders();
//                headers.set(HttpHeaders.CONTENT_TYPE, contentType);
//                headers.set(HttpHeaders.CONTENT_DISPOSITION, "inline"); // Affiche le fichier dans le navigateur
//                headers.set(HttpHeaders.CACHE_CONTROL, "public, max-age=3600"); // Cache pour 1 heure
//
//                return ResponseEntity.ok()
//                        .headers(headers)
//                        .body(responseBody);
//            } catch (IOException e) {
//                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
//            }
//        } else {
//            return ResponseEntity.notFound().build();
//        }
//    }


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

    @GetMapping(value = "/public/{documentID}/thumbnail")
    public ResponseEntity<Resource> getThumbnail(@PathVariable Integer documentID){
        try {
            Path file = thumbnailLocation.resolve(documentID + "-thumbnail.jpg");
            Resource resource = new UrlResource(file.toUri());

            if (resource.exists() || resource.isReadable()) {
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_TYPE, "image/jpeg")
                        .body(resource);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping(value = "/admin/update/{documentID}")
    @PreAuthorize("hasRole('ADMIN')")
    //@CachePut(value = "documentCache", key = "#documentID")
    public Document updateDocument(@PathVariable Integer documentID,
                                   @Valid @RequestBody Document document) throws IOException {
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Integer adminID = userDetails.getId();
        String message = "";
        try{
            Document document1 = this.documentService.updateDocument(documentID, adminID, document);
            message = "Document mis à jour avec succès...";
            return document1;
        }catch (Exception e){
            message = "Echec  de mis à jour du document...";
            throw new RuntimeException("Erreur lors de la mise à jour du document avec ID: " + documentID, e);
        }

    }
    @PutMapping(value = "/admin/delete/{documentID}")
    @PreAuthorize("hasRole('ADMIN')")
    //@CachePut(value = "documentCache", key = "#documentID")
    public Document deleteDocument(@PathVariable Integer documentID) throws IOException{
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Integer adminID = userDetails.getId();
        String message = "";
        try{
            Document document = this.documentService.deleteDocument(documentID, adminID);
            message = "Document supprimé avec succès...";
            return document;
        }catch (Exception e){
            message = "Echec  de suppression du document...";
            throw new RuntimeException("Erreur lors de la suppression du document avec ID: " + documentID, e);
        }
    }

    @GetMapping(value = "/public/search")
    public @ResponseBody List<Document> searchDocuments(@RequestParam String criteres){
        return this.documentService.rechercherDocument(criteres);
    }

//    @GetMapping(value = "/public/search")
//    public @ResponseBody List<Document> searchDocuments(@RequestParam String keywords){
//        return this.documentService.searchDocumentByKeyWords(keywords);
//    }

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


    @PutMapping(value = "/public/{id}/unlike")
    public ResponseEntity<String> unLikeIllustration(@PathVariable Integer id,
                                                   HttpServletRequest request){
        String utilIP = this.getClientIpAddress(request);
        String likeOrUnlike = "";

        if(this.likeIllustrationService.existingLike(id, utilIP)){
            LikeIllustration likeIllustration = this.likeIllustrationService.findLikedIllus(id, utilIP);
            if(likeIllustration.getMention() == Mention.unlike){
                likeIllustration.setMention(Mention.like);
                likeOrUnlike = "Illustration aimée...";
            }else if(likeIllustration.getMention() == Mention.like){
                likeIllustration.setMention(Mention.unlike);
                likeOrUnlike = "Illustration non aimée...";
            }
            this.likeIllustrationService.createLikeIllustration(likeIllustration);
            return ResponseEntity.ok(likeOrUnlike);
        }else{
            LikeIllustration likeIllustration = new LikeIllustration();
            likeIllustration.setDocumentID(id);
            likeIllustration.setUtilisateurIP(utilIP);
            likeIllustration.setMention(Mention.unlike);
            this.likeIllustrationService.createLikeIllustration(likeIllustration);
            if(likeIllustration.getMention() == Mention.unlike){
                likeOrUnlike = "Illustration non aimée...";
            }else {
                likeOrUnlike = "Illustration aimée...";
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


    @GetMapping(value = "/public/{documentID}/auteurs")
    public List<String> getAuteursDocument(@PathVariable Integer documentID){
        return this.documentService.auteursDocument(documentID);
    }

//    @GetMapping(value = "/public/{documentID}/thumbnail", produces = MediaType.IMAGE_JPEG_VALUE)
//    public @ResponseBody byte[] getDocumentThumbnail(@PathVariable Integer documentID,
//                                                     @RequestParam(required = false, defaultValue = "100") int width,
//                                                     @RequestParam(required = false, defaultValue = "100") int height) {
//        return documentService.getDocumentThumbnail(documentID, width, height);
//    }


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
