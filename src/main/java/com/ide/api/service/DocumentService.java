package com.ide.api.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ide.api.configurations.FilePaths;
import com.ide.api.dto.DocumentDTO;
import com.ide.api.entities.*;
import com.ide.api.enums.Langue;
import com.ide.api.enums.TypeFichier;
import com.ide.api.exception.FileNotFoundException;
import com.ide.api.message.DocumentCreationResponse;
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
import java.nio.file.NoSuchFileException;
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


    String thumbnailBasePath = FilePaths.THUMBNAIL_BASE_PATH;

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


    @Transactional
    public DocumentCreationResponse creerDocument(Document document,
                                                  List<Integer> idsCategorie,
                                                  List<Integer> idsTag,
                                                  List<Integer> idsAuteur) throws IOException {

        try {
            Document savedDocument = this.documentRepository.save(document);
            if (idsCategorie != null && !idsCategorie.isEmpty()) {
                for (Integer idCategorie : idsCategorie) {
                    Categorie categorie = this.categorieRepository.findById(idCategorie)
                            .orElseThrow(() -> new EntityNotFoundException("Categorie avec identifiant: " + idCategorie + " introuvable"));
                    CategorieDocument categorieDocument = new CategorieDocument();
                    categorieDocument.setDocument(savedDocument);
                    categorieDocument.setCategorie(categorie);
                    this.categorieDocumentRepository.save(categorieDocument);
                }
            }

            if (idsTag != null && !idsTag.isEmpty()) {
                for (Integer idTag : idsTag) {
                    Tag tag = this.tagRepository.findById(idTag)
                            .orElseThrow(() -> new EntityNotFoundException("Etiquette avec identifiant: " + idTag + " introuvable"));

                    DocumentTag documentTag = new DocumentTag();
                    documentTag.setDocument(savedDocument);
                    documentTag.setTag(tag);
                    this.documentTagRepository.save(documentTag);
                }
            }

            if (idsAuteur != null && !idsAuteur.isEmpty()) {
                for (Integer idAuteur : idsAuteur) {
                    Auteur auteur = this.auteurRepository.findById(idAuteur)
                            .orElseThrow(() -> new EntityNotFoundException("Auteur avec identifiant: " + idAuteur + " introuvable"));
                    AuteurDocument auteurDocument = new AuteurDocument();
                    auteurDocument.setDocument(savedDocument);
                    auteurDocument.setAuteur(auteur);
                    this.auteurDocumentRepository.save(auteurDocument);
                }
            }

            //generateAndSaveThumbnail(savedDocument.getDocumentID(), 300, 300);
            return new DocumentCreationResponse("Document ajouté et mis à jour avec succès ✅");
        } catch (EntityNotFoundException e) {
            System.err.println("Erreur lors de la recherche d'entité: " + e.getMessage());
            throw e;
        } catch (Exception e) {
            System.err.println("Erreur inattendue lors de la création du document: " + e.getMessage());
            throw new RuntimeException("Erreur inattendue lors de la création du document.", e);
        }
    }


    public List<Document> findDocuments() {
        List<Document> documents = new ArrayList<>();
        try {

            documents = this.documentRepository.findAll();
            documents = documents.stream().map(doc -> {
                try {
                    doc.setLike(this.likeIllustrationRepository.countLikes(doc.getDocumentID()));
                    doc.setUnlike(this.likeIllustrationRepository.countUnlikes(doc.getDocumentID()));
                } catch (Exception e) {
                    System.err.println("Erreur lors du comptage des likes ou unlikes pour le document ID " + doc.getDocumentID() + ": " + e.getMessage());
                }
                return doc;
            }).collect(Collectors.toList());

        } catch (Exception e) {
            System.err.println("Erreur lors de la récupération des documents: " + e.getMessage());
            documents = Collections.emptyList();
        }
        return documents;
    }

    public Optional<Document> findDocument(Integer id) {
        try {
            return this.documentRepository.findById(id);
        } catch (Exception e) {
            System.err.println("Erreur lors de la recherche du document avec ID " + id + ": " + e.getMessage());
            return Optional.empty();
        }
    }


    public byte[] getDocumentData(Integer id) throws IOException {
        try {
            Optional<Document> optionalDocument = findDocument(id);
            if (optionalDocument.isPresent()) {
                Document document = optionalDocument.get();
                Path filePath = Paths.get(document.getUrl());

                return Files.readAllBytes(filePath);
            } else {
                throw new RuntimeException("Document not found with id: " + id);
            }
        } catch (NoSuchFileException e) {
            System.err.println("Fichier non trouvé pour l'ID du document " + id + ": " + e.getMessage());
            throw new IOException("Le fichier associé au document n'existe pas.", e);
        } catch (IOException e) {
            System.err.println("Erreur de lecture du fichier pour l'ID du document " + id + ": " + e.getMessage());
            throw e;
        } catch (Exception e) {
            System.err.println("Erreur inattendue lors de la récupération des données du document avec ID " + id + ": " + e.getMessage());
            throw new RuntimeException("Erreur inattendue lors de la récupération des données du document.", e);
        }
    }


    public List<Document> findDocumentsByCategoryId(Categorie categorie) {
        List<Document> documents;
        try {

            documents = this.documentRepository.findByCategorieDocumentsCategorieID(categorie);


            return documents.stream().map(doc -> {
                try {
                    // Récupérer et mettre à jour les likes et unlikes
                    doc.setLike(this.likeIllustrationRepository.countLikes(doc.getDocumentID()));
                    doc.setUnlike(this.likeIllustrationRepository.countUnlikes(doc.getDocumentID()));
                } catch (Exception e) {

                    System.err.println("Erreur lors de la récupération des likes/unlikes pour le document ID " + doc.getDocumentID() + ": " + e.getMessage());
                    doc.setLike(0);
                    doc.setUnlike(0);
                }
                return doc;
            }).collect(Collectors.toList());

        } catch (Exception e) {

            e.printStackTrace();
            throw new RuntimeException("Erreur lors de la récupération des documents pour la catégorie ID " + categorie.getCategorieID() + ": " + e.getMessage(), e);
        }
    }

    public List<Document> findDocumentsByUtilisateurId(Utilisateur utilisateur) {
        List<Document> documents;
        try {
            documents = this.documentRepository.findByUtilisateurDocumentsUtilisateurID(utilisateur);
            return documents.stream().map(doc -> {
                try {
                    doc.setLike(this.likeIllustrationRepository.countLikes(doc.getDocumentID()));
                    doc.setUnlike(this.likeIllustrationRepository.countUnlikes(doc.getDocumentID()));
                } catch (Exception e) {

                    System.err.println("Erreur lors de la récupération des likes/unlikes pour le document ID " + doc.getDocumentID() + ": " + e.getMessage());
                    doc.setLike(0);
                    doc.setUnlike(0);
                }
                return doc;
            }).collect(Collectors.toList());

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Erreur lors de la récupération des documents pour l'utilisateur ID " + utilisateur.getUtilisateurID() + ": " + e.getMessage(), e);
        }
    }


    public List<Document> findDocumentsByAuteurId(Auteur auteur) {
        List<Document> documents;
        try {

            documents = this.documentRepository.findByAuteurDocumentsAuteurID(auteur);


            return documents.stream().map(doc -> {
                try {

                    doc.setLike(this.likeIllustrationRepository.countLikes(doc.getDocumentID()));
                    doc.setUnlike(this.likeIllustrationRepository.countUnlikes(doc.getDocumentID()));
                } catch (Exception e) {

                    System.err.println("Erreur lors de la récupération des likes/unlikes pour le document ID " + doc.getDocumentID() + ": " + e.getMessage());
                    doc.setLike(0);
                    doc.setUnlike(0);
                }
                return doc;
            }).collect(Collectors.toList());

        } catch (Exception e) {

            e.printStackTrace();
            throw new RuntimeException("Erreur lors de la récupération des documents pour l'auteur ID " + auteur.getAuteurID() + ": " + e.getMessage(), e);
        }
    }

    public List<Document> findDocumentsByTagId(Tag tag) {
        List<Document> documents;
        try {
            documents = this.documentRepository.findByDocumentTagsDocumentID(tag);

            return documents.stream().map(doc -> {
                try {
                    doc.setLike(this.likeIllustrationRepository.countLikes(doc.getDocumentID()));
                    doc.setUnlike(this.likeIllustrationRepository.countUnlikes(doc.getDocumentID()));
                } catch (Exception e) {
                    System.err.println("Erreur lors de la récupération des likes/unlikes pour le document ID " + doc.getDocumentID() + ": " + e.getMessage());
                    doc.setLike(0);
                    doc.setUnlike(0);
                }
                return doc;
            }).collect(Collectors.toList());

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Erreur lors de la récupération des documents pour le tag ID " + tag.getTagID() + ": " + e.getMessage(), e);
        }
    }

    @Transactional
    public List<Document> rechercherDocument(String mots) {
        List<Document> documents;
        try {
            documents = this.documentRepository.rechercher(mots);
            return documents.stream().map(doc -> {
                try {
                    // Récupérer et mettre à jour les likes et unlikes pour chaque document
                    doc.setLike(this.likeIllustrationRepository.countLikes(doc.getDocumentID()));
                    doc.setUnlike(this.likeIllustrationRepository.countUnlikes(doc.getDocumentID()));
                } catch (Exception e) {
                    // Loguer l'erreur et mettre à jour avec des valeurs par défaut en cas d'échec
                    System.err.println("Erreur lors de la récupération des likes/unlikes pour le document ID " + doc.getDocumentID() + ": " + e.getMessage());
                    doc.setLike(0);
                    doc.setUnlike(0);
                }
                return doc;
            }).collect(Collectors.toList());

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Erreur lors de la recherche des documents avec les mots-clés '" + mots + "': " + e.getMessage(), e);
        }
    }

    @Transactional
    public List<Document> searchDocumentByKeyWords(String keywords) {
        List<Document> documents = new ArrayList<>();

        try {
            String[] keywordArray = keywords.split(",");
            if (keywordArray.length == 1) {
                documents.addAll(documentRepository.searchDocumentByKeyWords(keywordArray[0].trim()));
            } else {
                Set<Document> uniqueDocuments = new HashSet<>();
                for (String keyword : keywordArray) {
                    uniqueDocuments.addAll(documentRepository.searchDocumentByKeyWords(keyword.trim()));
                }
                documents.addAll(uniqueDocuments);
            }
            return documents.stream().map(doc -> {
                try {
                    doc.setLike(this.likeIllustrationRepository.countLikes(doc.getDocumentID()));
                    doc.setUnlike(this.likeIllustrationRepository.countUnlikes(doc.getDocumentID()));
                } catch (Exception e) {
                    System.err.println("Erreur lors de la récupération des likes/unlikes pour le document ID " + doc.getDocumentID() + ": " + e.getMessage());
                    doc.setLike(0);
                    doc.setUnlike(0);
                }
                return doc;
            }).collect(Collectors.toList());

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Erreur lors de la recherche des documents avec les mots-clés '" + keywords + "': " + e.getMessage(), e);
        }
    }

    public List<Document> searchDocument(String searchTerm) {
        List<Document> documents = new ArrayList<>();

        try {
            String[] keywords = searchTerm.split(",");
            List<Specification<Document>> specs = new ArrayList<>();
            for (String keyword : keywords) {
                specs.add(new DocumentSpecification(keyword.trim()));
            }
            Specification<Document> resultSpec = specs.stream()
                    .reduce(Specification::or)
                    .orElse(null);

            if (resultSpec != null) {
                documents = documentRepository.findAll(resultSpec);
            }
            return documents.stream().map(doc -> {
                try {
                    doc.setLike(this.likeIllustrationRepository.countLikes(doc.getDocumentID()));
                    doc.setUnlike(this.likeIllustrationRepository.countUnlikes(doc.getDocumentID()));
                } catch (Exception e) {
                    System.err.println("Erreur lors de la récupération des likes/unlikes pour le document ID " + doc.getDocumentID() + ": " + e.getMessage());
                    doc.setLike(0);
                    doc.setUnlike(0);
                }
                return doc;
            }).collect(Collectors.toList());

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Erreur lors de la recherche des documents avec le terme de recherche '" + searchTerm + "': " + e.getMessage(), e);
        }
    }


    public List<Document> findDocumentsByType(TypeFichier typeFichier) {
        List<Document> documents = new ArrayList<>();

        try {
            documents = this.documentRepository.findByTypeFichier(typeFichier);
            return documents.stream().map(doc -> {
                try {
                    doc.setLike(this.likeIllustrationRepository.countLikes(doc.getDocumentID()));
                    doc.setUnlike(this.likeIllustrationRepository.countUnlikes(doc.getDocumentID()));
                } catch (Exception e) {
                    System.err.println("Erreur lors de la récupération des likes/unlikes pour le document ID " + doc.getDocumentID() + ": " + e.getMessage());
                    doc.setLike(0);
                    doc.setUnlike(0);
                }
                return doc;
            }).collect(Collectors.toList());

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Erreur lors de la recherche des documents avec le type de fichier '" + typeFichier + "': " + e.getMessage(), e);
        }
    }


    public List<Document> getDocumentsSortedBy(String sortedBy) {
        List<Document> documents = new ArrayList<>();

        try {
            switch (sortedBy) {
                case "date":
                    documents = this.documentRepository.findAllByOrderDateCreationDocumentDesc();
                    break;
                case "Anglais":
                case "Français":
                case "Créole":
                case "Espagnol":
                    documents = this.documentRepository.findAllByOrderLangue(sortedBy);
                    break;
                case "titre":
                    documents = this.documentRepository.findAllByOrderTitre();
                    break;
                default:
                    documents = this.documentRepository.findAll();
                    break;
            }

            return documents.stream().map(doc -> {
                try {
                    doc.setLike(this.likeIllustrationRepository.countLikes(doc.getDocumentID()));
                    doc.setUnlike(this.likeIllustrationRepository.countUnlikes(doc.getDocumentID()));
                } catch (Exception e) {
                    // Gérer les erreurs de comptage des likes/unlikes
                    System.err.println("Erreur lors de la récupération des likes/unlikes pour le document ID " + doc.getDocumentID() + ": " + e.getMessage());
                    doc.setLike(0);
                    doc.setUnlike(0);
                }
                return doc;
            }).collect(Collectors.toList());

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Erreur lors de la récupération des documents triés par '" + sortedBy + "': " + e.getMessage(), e);
        }
    }


    private String saveThumbnailToFileSystem(byte[] thumbnailData, Integer fileId) throws IOException {

        String thumbnailFileName = fileId + "-thumbnail.jpg";
        String thumbnailFilePath = thumbnailBasePath + thumbnailFileName;

        Path directoryPath = Paths.get(thumbnailBasePath);
        if (!Files.exists(directoryPath)) {
            try {
                Files.createDirectories(directoryPath);
            } catch (IOException e) {
                throw new IOException("Erreur lors de la création du répertoire pour les miniatures : " + directoryPath, e);
            }
        }

        try {
            Files.write(Paths.get(thumbnailFilePath), thumbnailData);
        } catch (IOException e) {
            throw new IOException("Erreur lors de la sauvegarde de la miniature : " + thumbnailFilePath, e);
        }

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
                document.setThumbnail(thumbnailUrl);
                documentRepository.save(document);
            } else {
                throw new IllegalArgumentException("Document non trouvé pour l'ID: " + documentId);
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Echec de création de minuature pour le document de l'ID : " + documentId, e);
        } catch (IllegalArgumentException e) {
            System.err.println("Erreur : " + e.getMessage());
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Erreur inattendue lors de la génération et la sauvegarde de minuature pour le document ID: ", e);
        }
    }


}
