package com.ide.api.entities;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import javax.persistence.OneToMany;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


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
    private int categorId;

    //Le nom de la categorie
    private String nom;

    //Une liste permettant de stocker les différents documents qui appartiennent à une categorie

    @ManyToMany(fetch = FetchType.LAZY,
            cascade = {
                    CascadeType.PERSIST,
                    CascadeType.MERGE
            },mappedBy = "categories")
    @JsonIgnore
    private Set<Document> documents = new HashSet<Document>();;

    public Categorie() {
    }

    public Categorie(int categorId, String nom, Set<Document> documents) {
        this.categorId = categorId;
        this.nom = nom;
        this.documents = documents;
    }

    public int getCategorId() {
        return categorId;
    }

    public void setCategorId(int id) {
        this.categorId = categorId;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public Set<Document> getDocuments() {
        return documents;
    }

    public void setDocuments(Set<Document> documents) {
        this.documents = documents;
    }
}
