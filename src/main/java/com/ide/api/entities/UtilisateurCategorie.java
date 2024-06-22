package com.ide.api.entities;

import com.fasterxml.jackson.annotation.*;
import com.ide.api.enums.TypeGestion;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Date;

@Entity
@DynamicInsert
@IdClass(UtilisateurCategorieID.class)
@Table(name = "tableAdminCategorie")
public class UtilisateurCategorie implements Serializable {
    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "IdentifiantAdmin")
    private Utilisateur utilisateurID;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "IdentifiantCategorie")
    private Categorie categorieID;

    @Column(name = "DATE")
    private Timestamp dateCreation;

    @Column(name = "TypeGestion")
    @Enumerated(EnumType.STRING)
    private TypeGestion typeGestion;
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

    public Timestamp getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(Timestamp dateCreation) {
        this.dateCreation = dateCreation;
    }

    public Utilisateur getUtilisateurID() {
        return utilisateurID;
    }

    public void setUtilisateurID(Utilisateur utilisateurID) {
        this.utilisateurID = utilisateurID;
    }

    public Categorie getCategorieID() {
        return categorieID;
    }

    public void setCategorieID(Categorie categorieID) {
        this.categorieID = categorieID;
    }

    public TypeGestion getTypeGestion() {
        return typeGestion;
    }

    public void setTypeGestion(TypeGestion typeGestion) {
        this.typeGestion = typeGestion;
    }
}
