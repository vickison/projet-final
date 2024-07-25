package com.ide.api.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ide.api.dto.DocumentDTO;
import com.ide.api.entities.*;
import com.ide.api.enums.Langue;
import com.ide.api.enums.TypeFichier;
import com.ide.api.exception.FileNotFoundException;
import com.ide.api.message.ResponseDocument;
import com.ide.api.repository.*;
import com.ide.api.utilities.DocumentSpecification;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Service
public class DocumentService {
    private DocumentRepository documentRepository;
    private CategorieRepository categorieRepository;
    private CategorieDocumentRepository categorieDocumentRepository;
    private TagRepository tagRepository;
    private DocumentTagRepository documentTagRepository;
    private AuteurRepository auteurRepository;
    private AuteurDocumentRepository auteurDocumentRepository;
    private LikeIllustrationRepository likeIllustrationRepository;
    private ThumbnailService thumbnailService;

    private final String thumbnailBasePath = "C:\\Users\\avicky\\libeil\\thumbnail\\";

    public DocumentService(DocumentRepository documentRepository,
                           CategorieRepository categorieRepository,
                           CategorieDocumentRepository categorieDocumentRepository,
                           TagRepository tagRepository,
                           DocumentTagRepository documentTagRepository,
                           AuteurRepository auteurRepository,
                           AuteurDocumentRepository auteurDocumentRepository,
                           LikeIllustrationRepository likeIllustrationRepository,
                           ThumbnailService thumbnailService) {
        this.documentRepository = documentRepository;
        this.categorieRepository = categorieRepository;
        this.categorieDocumentRepository = categorieDocumentRepository;
        this.tagRepository = tagRepository;
        this.documentTagRepository = documentTagRepository;
        this.auteurRepository = auteurRepository;
        this.auteurDocumentRepository = auteurDocumentRepository;
        this.likeIllustrationRepository = likeIllustrationRepository;
        this.thumbnailService = thumbnailService;
    }

    public void addDocument(Document document) throws IOException {
        this.documentRepository.save(document);
        System.out.println(this.documentRepository.save(document));
    }

    public void creerDocument(Document document,
                              List<Integer> idsCategorie,
                              List<Integer> idsTag,
                              List<Integer> idsAuteur) throws IOException{
        System.out.println("---------------------------");
        Document savedDocument = this.documentRepository.save(document);
        generateAndSaveThumbnail(savedDocument.getDocumentID(), 300, 300);
        System.out.println("Service-----------------------------");
        if(idsCategorie != null && !idsCategorie.isEmpty()){
            CategorieDocument categorieDocument = new CategorieDocument();
            for(Integer idCategorie : idsCategorie){
                Categorie categorie = this.categorieRepository.findById(idCategorie)
                        .orElseThrow(() -> new EntityNotFoundException("Categorie avec identifiant: " + idCategorie + " introuvable"));

                categorieDocument.setDocument(savedDocument);
                categorieDocument.setCategorie(categorie);

                this.categorieDocumentRepository.save(categorieDocument);

            }
        }
        if(idsTag != null && !idsTag.isEmpty()){
            DocumentTag documentTag = new DocumentTag();
            for(Integer idTag: idsTag){
                Tag tag = this.tagRepository.findById(idTag)
                        .orElseThrow(() -> new EntityNotFoundException("Categorie avec identifiant: " + idTag + " introuvable"));

                documentTag.setDocument(savedDocument);
                documentTag.setTag(tag);

                this.documentTagRepository.save(documentTag);
            }
        }
        if(idsAuteur != null && !idsAuteur.isEmpty()){
            AuteurDocument auteurDocument = new AuteurDocument();
            for(Integer idAuteur: idsAuteur){
                Auteur auteur = this.auteurRepository.findById(idAuteur)
                        .orElseThrow(() -> new EntityNotFoundException("Categorie avec identifiant: " + idAuteur + " introuvable"));

                auteurDocument.setDocument(savedDocument);
                auteurDocument.setAuteur(auteur);

                this.auteurDocumentRepository.save(auteurDocument);
            }
        }
    }

    public List<Document> findDocuments(){
        List<Document> documents = this.documentRepository.findAll();
        return documents.stream().map(doc -> {
            doc.setLike(this.likeIllustrationRepository.countLikes(doc.getDocumentID()));
            doc.setUnlike(this.likeIllustrationRepository.countUnlikes(doc.getDocumentID()));
            return doc;
        }).collect(Collectors.toList());
    }

    public Optional<Document> findDocument(Integer id){
        return this.documentRepository.findById(id);
    }

    public byte[] getDocumentData(Integer id) throws IOException {
        Optional<Document> optionalDocument = findDocument(id);
        if (optionalDocument.isPresent()) {
            Document document = optionalDocument.get();
            // Récupérer les données binaires du fichier à partir de l'URL ou de l'emplacement spécifié
            Path filePath = Paths.get(document.getUrl()); // Supposant que getUrl() retourne l'URL du fichier
            return Files.readAllBytes(filePath);
        } else {
            throw new RuntimeException("Document not found with id: " + id);
        }
    }
    public List<Document> findDocumentsByCategoryId(Categorie categorie){
        List<Document> documents = this.documentRepository.findByCategorieDocumentsCategorieID(categorie);
        return documents.stream().map(doc -> {
            doc.setLike(this.likeIllustrationRepository.countLikes(doc.getDocumentID()));
            doc.setUnlike(this.likeIllustrationRepository.countUnlikes(doc.getDocumentID()));
            return doc;
        }).collect(Collectors.toList());
    }

    public List<Document> findDocumentsByUtilisateurId(Utilisateur utilisateur){
        return this.documentRepository.findByUtilisateurDocumentsUtilisateurID(utilisateur);
    }

    public List<Document> findDocumentsByAuteurId(Auteur auteur){
        return this.documentRepository.findByAuteurDocumentsAuteurID(auteur);
    }

    public List<Document> findDocumentsByTagId(Tag tag){
        List<Document> documents = this.documentRepository.findByDocumentTagsDocumentID(tag);
        return documents.stream().map(doc -> {
            doc.setLike(this.likeIllustrationRepository.countLikes(doc.getDocumentID()));
            doc.setUnlike(this.likeIllustrationRepository.countUnlikes(doc.getDocumentID()));
            return doc;
        }).collect(Collectors.toList());
    }

    @Transactional
    public List<Document> rechercherDocument(String mots){
        List<Document> documents = this.documentRepository.rechercher(mots);
        return documents.stream().map(doc -> {
            doc.setLike(this.likeIllustrationRepository.countLikes(doc.getDocumentID()));
            doc.setUnlike(this.likeIllustrationRepository.countUnlikes(doc.getDocumentID()));
            return doc;
        }).collect(Collectors.toList());
    }
    @Transactional
    public List<Document> searchDocumentByKeyWords(String keywords){
        String[] keywordArray= keywords.split(",");
        List<Document> documents = new ArrayList<>();
        if(keywordArray.length == 1){
            documents.addAll(documentRepository.searchDocumentByKeyWords(keywordArray[0].trim()));
        }else{
            for(String keyword: keywordArray){
                documents.addAll(documentRepository.searchDocumentByKeyWords(keyword.trim()));
            }
        }
        return documents.stream().map(doc -> {
            doc.setLike(this.likeIllustrationRepository.countLikes(doc.getDocumentID()));
            doc.setUnlike(this.likeIllustrationRepository.countUnlikes(doc.getDocumentID()));
            return doc;
        }).collect(Collectors.toList());
        //return documents;
    }

    public List<Document> searchDocument(String searchTerm){
        String[] keywords= searchTerm.split(",");
        List<Specification<Document>> specs = new ArrayList<>();
        for(String keyword: keywords){
            specs.add(new DocumentSpecification(keyword));
        }
        Specification<Document> resultSpec = specs.stream().reduce(Specification::or).orElse(null);
        List<Document> documents = documentRepository.findAll(resultSpec);
        return documents.stream().map(doc -> {
            doc.setLike(this.likeIllustrationRepository.countLikes(doc.getDocumentID()));
            doc.setUnlike(this.likeIllustrationRepository.countUnlikes(doc.getDocumentID()));
            return doc;
        }).collect(Collectors.toList());

    }

    public List<Document> findDocumentsByType(TypeFichier typeFichier){
        List<Document> documents = this.documentRepository.findByTypeFichier(typeFichier);
        return documents.stream().map(doc -> {
            doc.setLike(this.likeIllustrationRepository.countLikes(doc.getDocumentID()));
            doc.setUnlike(this.likeIllustrationRepository.countUnlikes(doc.getDocumentID()));
            return doc;
        }).collect(Collectors.toList());

    }

    //@Transactional
    public  List<Document> getDocumentsSortedBy(String sortedBy){
        switch (sortedBy){
            case "date":
                List<Document> documents = this.documentRepository.findAllByOrderDateCreationDocumentDesc();
                return documents.stream().map(doc -> {
                    doc.setLike(this.likeIllustrationRepository.countLikes(doc.getDocumentID()));
                    doc.setUnlike(this.likeIllustrationRepository.countUnlikes(doc.getDocumentID()));
                    return doc;
                }).collect(Collectors.toList());
            case "Anglais":
            case "Français":
            case "Créole":
            case "Espagnol":
                return this.documentRepository.findAllByOrderLangue(sortedBy).stream().map(doc -> {
                    doc.setLike(this.likeIllustrationRepository.countLikes(doc.getDocumentID()));
                    doc.setUnlike(this.likeIllustrationRepository.countUnlikes(doc.getDocumentID()));
                    return doc;
                }).collect(Collectors.toList());
            case "titre":
                return this.documentRepository.findAllByOrderTitre().stream().map(doc -> {
                    doc.setLike(this.likeIllustrationRepository.countLikes(doc.getDocumentID()));
                    doc.setUnlike(this.likeIllustrationRepository.countUnlikes(doc.getDocumentID()));
                    return doc;
                }).collect(Collectors.toList());
            default:
                return this.documentRepository.findAll().stream().map(doc -> {
                    doc.setLike(this.likeIllustrationRepository.countLikes(doc.getDocumentID()));
                    doc.setUnlike(this.likeIllustrationRepository.countUnlikes(doc.getDocumentID()));
                    return doc;
                }).collect(Collectors.toList());
        }
    }

//    public byte[] getDocumentThumbnail(Integer documentID, int width, int height) {
//        Document document = documentRepository.findById(documentID)
//                .orElseThrow(() -> new RuntimeException("Document not found with id: " + documentID));
//
//        // Récupérer la miniature correspondante au document
//        try {
//            return thumbnailService.generateThumbnail(document.getDocumentID(), width, height);
//        } catch (IOException e) {
//            throw new RuntimeException("Failed to generate thumbnail for document with id: " + documentID, e);
//        }
//    }

    private String saveThumbnailToFileSystem(byte[] thumbnailData, Integer fileId) throws IOException {
        // Sauvegarder la miniature sur le système de fichiers
        String thumbnailFileName = fileId + "-thumbnail.jpg";
        String thumbnailFilePath = thumbnailBasePath + thumbnailFileName;
        Files.write(Paths.get(thumbnailFilePath), thumbnailData);
        return thumbnailFilePath;
    }

    public void generateAndSaveThumbnail(Integer documentId, int thumbnailWidth, int thumbnailHeight) {
        try {
            Optional<Document> documentOptional = documentRepository.findById(documentId);
            if (documentOptional.isPresent()) {
                Document document = documentOptional.get();
                byte[] fileData = getDocumentData(documentId);
                byte[] thumbnailData = thumbnailService.generateThumbnail(fileData, documentId, document.getUrl(), thumbnailWidth, thumbnailHeight);
                String thumbnailUrl = thumbnailService.saveThumbnailToFileSystemStr(thumbnailData, documentId);

                // Mettre à jour l'URL de la miniature dans l'entité Document
                document.setThumbnail(thumbnailUrl);
                documentRepository.save(document); // Mettre à jour l'entité dans la base de données avec l'URL de la miniature
            } else {
                // Gérer le cas où le document avec l'ID donné n'est pas trouvé
                throw new IllegalArgumentException("Document not found for id: " + documentId);
            }
        } catch (IOException e) {
            // Gérer les exceptions liées à la génération ou à la sauvegarde de la miniature
            e.printStackTrace();
            throw new RuntimeException("Failed to generate or save thumbnail for document id: " + documentId, e);
        }
    }

}
