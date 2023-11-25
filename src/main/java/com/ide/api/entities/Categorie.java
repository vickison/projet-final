package com.ide.api.entities;

import com.fasterxml.jackson.annotation.*;
import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.*;


@Entity
@Table(name = "categorie")
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "categorieID")
public class Categorie{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer categorieID;
    private String nom;

    @OneToMany(mappedBy = "categorieID", cascade = CascadeType.ALL)
    private Set<CategorieDocument> categorieDocuments = new HashSet<>();

    @OneToMany(mappedBy = "categorieID", cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<UtilisateurCategorie> utilisateurCategories = new HashSet<>();

    public Integer getCategorieID() {
        return categorieID;
    }

    public void setCategorieID(Integer categorieID) {
        this.categorieID = categorieID;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public Set<CategorieDocument> getCategorieDocuments() {
        return categorieDocuments;
    }

    public void setCategorieDocuments(Set<CategorieDocument> categorieDocuments) {
        this.categorieDocuments = categorieDocuments;
    }

    public Set<UtilisateurCategorie> getUtilisateurCategories() {
        return utilisateurCategories;
    }

    public void setUtilisateurCategories(Set<UtilisateurCategorie> utilisateurCategories) {
        this.utilisateurCategories = utilisateurCategories;
    }
}

