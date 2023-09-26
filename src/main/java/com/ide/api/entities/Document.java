package com.ide.api.entities;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.persistence.Entity;
import java.time.Instant;
import java.util.Set;


/*

On definit ici une classe Document, qui est une entité,
donc qui va nous créer, grace à des annotations, la table documents dans la base avec les champs nécéssaires

* */
@EntityListeners(AuditingEntityListener.class)
@Entity
@Table(name = "documents")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Document {

    //la clé primaire
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer documentId;

    //Le titre du document, ici on a considéré titre comme le nom du document
    private String titre;

    //Une briève description du document
    @Lob
    private String description;


    //La date d'ajout du document
    @CreatedDate
    private Instant creation;

    //Le type du document(application/pdf, image/png,...)
    private String type;

    //Le size(bytes ou kilobytes ou megabytes)
    @Lob
    private byte[] data;

    private String url;


    @JsonIgnore
    @ManyToMany
    @JoinTable(
            name = "Categorie_document",
            joinColumns = @JoinColumn(name = "documentId"),
            inverseJoinColumns = @JoinColumn(name = "categorieId"))
    private Set<Categorie> categories;

    @ManyToMany
    @JoinTable(
            name = "auteur_document",
            joinColumns = @JoinColumn(name = "documentId"),
            inverseJoinColumns = @JoinColumn(name = "auteurId"))
    @JsonIgnore
    private Set<Auteur> auteurs;

    public Document() {
    }

    public Document(int documentId, String titre, String description, Instant creation, String type, byte[] data, String url, Set<Categorie> categories, Set<Auteur> auteurs) {
        this.documentId = documentId;
        this.titre = titre;
        this.description = description;
        this.creation = creation;
        this.type = type;
        this.data = data;
        this.url = url;
        this.categories = categories;
        this.auteurs = auteurs;
    }

    public Integer getDocumentId() {
        return documentId;
    }

    public void setDocumentId(int documentId) {
        this.documentId = documentId;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Instant getCreation() {
        return creation;
    }

    public void setCreation(Instant creation) {
        this.creation = creation;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Set<Categorie> getCategories() {
        return categories;
    }

    public void setCategories(Set<Categorie> categories) {
        this.categories = categories;
    }

    public Set<Auteur> getAuteurs() {
        return auteurs;
    }

    public void setAuteurs(Set<Auteur> auteurs) {
        this.auteurs = auteurs;
    }
}
