package com.ide.api.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Auteur{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer auteurId;
    private String nom;
    private String prenom;


    @ManyToMany(fetch = FetchType.LAZY,
            cascade = {
                    CascadeType.PERSIST,
                    CascadeType.MERGE
            },mappedBy = "auteurs")
    @JsonIgnore
    private Set<Document> documents = new HashSet<Document>();;

    public Auteur() {
    }

    public Auteur(Integer auteurId, String nom, String prenom, Set<Document> documents) {
        this.auteurId = auteurId;
        this.nom = nom;
        this.prenom = prenom;
        this.documents = documents;
    }

    public Integer getAuteurId() {
        return auteurId;
    }

    public void setAuteurId(Integer auteurId) {
        this.auteurId = auteurId;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    @ManyToMany(mappedBy = "auteurs")
    public Set<Document> getDocuments() {
        return documents;
    }

    public void setDocuments(Set<Document> documents) {
        this.documents = documents;
    }
}
