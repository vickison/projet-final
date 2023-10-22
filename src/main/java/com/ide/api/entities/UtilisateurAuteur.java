package com.ide.api.entities;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import javax.persistence.*;
import java.util.Date;

@Entity
@IdClass(UtilisateurAuteurID.class)
@Table(name = "utilisateur_auteur")
public class UtilisateurAuteur {

    @Id
    @ManyToOne
    @JoinColumn(name = "utilisateurID")
    private Utilisateur utilisateurID;

    @Id
    @ManyToOne
    @JoinColumn(name = "auteurID")
    private Auteur auteurID;

    @Column(name = "date_creation")
    private Date dateCreation;

    public Utilisateur getUtilisateur() {
        return utilisateurID;
    }

    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateurID = utilisateur;
    }

    public Auteur getAuteur() {
        return auteurID;
    }

    public void setAuteur(Auteur auteur) {
        this.auteurID = auteur;
    }

    public Date getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(Date dateCreation) {
        this.dateCreation = dateCreation;
    }
}
