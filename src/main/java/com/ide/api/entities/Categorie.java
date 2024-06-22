package com.ide.api.entities;

import com.fasterxml.jackson.annotation.*;
import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.*;


@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "tableCategories")
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "categorieID")
public class Categorie{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idCategorie")
    private Integer categorieID;
    @Column(name = "CATEGORIE")
    private String nom;

    private Timestamp DateCreationCategorie;
    private String AuteurCreationCategorie;
    @UpdateTimestamp
    private Timestamp DateModificationCategorie;
    private String AuteurModificationCategorie;
    private boolean SupprimerCategorie;

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

    public Timestamp getDateCreationCategorie() {
        return DateCreationCategorie;
    }

    public void setDateCreationCategorie(Timestamp dateCreationCategorie) {
        DateCreationCategorie = dateCreationCategorie;
    }

    public String getAuteurCreationCategorie() {
        return AuteurCreationCategorie;
    }

    public void setAuteurCreationCategorie(String auteurCreationCategorie) {
        AuteurCreationCategorie = auteurCreationCategorie;
    }

    public Timestamp getDateModificationCategorie() {
        return DateModificationCategorie;
    }

    public void setDateModificationCategorie(Timestamp dateModificationCategorie) {
        DateModificationCategorie = dateModificationCategorie;
    }

    public String getAuteurModificationCategorie() {
        return AuteurModificationCategorie;
    }

    public void setAuteurModificationCategorie(String auteurModificationCategorie) {
        AuteurModificationCategorie = auteurModificationCategorie;
    }

    public boolean getSupprimerCategorie() {
        return SupprimerCategorie;
    }

    public void setSupprimerCategorie(boolean supprimerCategorie) {
        SupprimerCategorie = supprimerCategorie;
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

