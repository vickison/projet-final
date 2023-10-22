package com.ide.api.entities;

import com.fasterxml.jackson.annotation.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Date;

@Entity
@IdClass(UtilisateurCategorieID.class)
@Table(name = "utilisateur_categorie")
public class UtilisateurCategorie {
    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "utilisateurID")
    @JsonBackReference(value = "ut-cat")
    private Utilisateur utilisateurID;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categorieID")
    @JsonBackReference(value = "cat-ut")
    private Categorie categorieID;

    @Column(name = "date_creation")
    private Date dateCreation;

    public Utilisateur getUtilisateur() {
        return utilisateurID;
    }

    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateurID = utilisateur;
    }

    public Categorie getCategorie() {
        return categorieID;
    }

    public void setCategorie(Categorie categorie) {
        this.categorieID = categorie;
    }

    public Date getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(Date dateCreation) {
        this.dateCreation = dateCreation;
    }
}
