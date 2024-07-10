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

    public DocumentService(DocumentRepository documentRepository,
                           CategorieRepository categorieRepository,
                           CategorieDocumentRepository categorieDocumentRepository,
                           TagRepository tagRepository,
                           DocumentTagRepository documentTagRepository,
                           AuteurRepository auteurRepository,
                           AuteurDocumentRepository auteurDocumentRepository,
                           LikeIllustrationRepository likeIllustrationRepository) {
        this.documentRepository = documentRepository;
        this.categorieRepository = categorieRepository;
        this.categorieDocumentRepository = categorieDocumentRepository;
        this.tagRepository = tagRepository;
        this.documentTagRepository = documentTagRepository;
        this.auteurRepository = auteurRepository;
        this.auteurDocumentRepository = auteurDocumentRepository;
        this.likeIllustrationRepository = likeIllustrationRepository;
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
        return this.documentRepository.findByDocumentTagsDocumentID(tag);
    }

    @Transactional
    public List<Document> rechercherDocument(String mots){
        return this.documentRepository.rechercher(mots);
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
        return documents;
    }

    public List<Document> searchDocument(String searchTerm){
        String[] keywords= searchTerm.split(",");
        List<Specification<Document>> specs = new ArrayList<>();
        for(String keyword: keywords){
            specs.add(new DocumentSpecification(keyword));
        }
        Specification<Document> resultSpec = specs.stream().reduce(Specification::or).orElse(null);
        return documentRepository.findAll(resultSpec);
    }

    public List<Document> findDocumentsByType(TypeFichier typeFichier){
        return this.documentRepository.findByTypeFichier(typeFichier);
    }

    //@Transactional
    public  List<Document> getDocumentsSortedBy(String sortedBy){
        switch (sortedBy){
            case "date":
                return documentRepository.findAllByOrderDateCreationDocumentDesc();
            case "Anglais":
            case "Français":
            case "Créole":
            case "Espagnol":
                return documentRepository.findAllByOrderLangue(sortedBy);
            case "titre":
                return documentRepository.findAllByOrderTitre();
            default:
                return documentRepository.findAll();
        }
    }

}
