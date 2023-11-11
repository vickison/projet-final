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
    @Column(name = "file_id")
    private int file_id;

    //Le titre du document, ici on a considéré titre comme le nom du document
    private String titre;

    //Une briève description du document
    @Lob
    private String description;


    //La date d'ajout du document
    @CreatedDate
    @Column(name = "created_at", nullable = false)
    private Instant date_creation;

    //Le type du document(application/pdf, image/png,...)
    private String type;

    //Le size(bytes ou kilobytes ou megabytes)
    @Lob
    private byte[] data;

    //La categorie à laquelle appartient le document, clé étrangère
    @ManyToOne(targetEntity = Categorie.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_cat_id")
    private Categorie categorie;

    //Constructeur sans params
    public Document() {
    }

    //Constructeur avec params
    public Document(Integer file_id, String titre, String description, Instant date_creation, String type, byte[] data, Categorie categorie) {
        this.file_id = file_id;
        this.titre = titre;
        this.description = description;
        this.date_creation = date_creation;
        this.type = type;
        this.data = data;
        this.categorie = categorie;
    }


    //Setters et Getters

    public Integer getFile_id() {
        return file_id;
    }

    public void setFile_id(Integer file_id) {
        this.file_id = file_id;
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

    public Instant getDate_creation() {
        return date_creation;
    }

    public void setDate_creation(Instant date_creation) {
        this.date_creation = date_creation;
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

    @ManyToOne(targetEntity = Categorie.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "fk_cat_id", nullable = false)
    @JsonBackReference
    public Categorie getCategorie() {
        return categorie;
    }

    public void setCategorie(Categorie categorie) {
        this.categorie = categorie;
    }
}
