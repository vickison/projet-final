package com.ide.api.entities;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import javax.persistence.OneToMany;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/*

On definit ici une classe Categorie, qui est une entité,
donc qui va nous créer, grace à des annotations, la table categories dans la base avec les champs nécéssaires

* */
@Entity
@Table(name = "categories")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Categorie implements Serializable {

    //La clé primaire de la table
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cat_id")
    private int cat_id;

    //Le nom de la categorie
    private String nom;

    //Une liste permettant de stocker les différents documents qui appartiennent à une categorie
    @OneToMany(targetEntity = Document.class, cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy="categorie")
    @Fetch(FetchMode.JOIN)
    private List<Document> documents;

    //Contructeur sans params
    public Categorie() {
    }

    //Constructeur avec params
    public Categorie(Integer cat_id, String nom, List<Document> documents) {
        this.cat_id = cat_id;
        this.nom = nom;
        this.documents = documents;
    }

    //Getters et Setters
    public Integer getCat_id() {
        return cat_id;
    }

    public void setCat_id(Integer cat_id) {
        this.cat_id = cat_id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    @OneToMany(targetEntity = Document.class, fetch = FetchType.EAGER, mappedBy="categorie")
    @JsonManagedReference
    public List<Document> getDocuments() {
        return documents;
    }

    public void setDocuments(List<Document> documents) {
        this.documents = documents;
    }
}
