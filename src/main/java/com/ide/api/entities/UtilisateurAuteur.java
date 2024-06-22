package com.ide.api.entities;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.ide.api.enums.TypeGestion;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@DynamicInsert
@IdClass(UtilisateurAuteurID.class)
@Table(name = "tableAdminAuteur")
public class UtilisateurAuteur implements Serializable {

    @Id
    @ManyToOne
    @JoinColumn(name = "IdentifiantAdmin")
    private Utilisateur utilisateurID;

    @Id
    @ManyToOne
    @JoinColumn(name = "IdentifiantAuteur")
    private Auteur auteurID;

    @Column(name = "DATE")
    private Date dateCreation;
    @Column(name = "TypeGestion")
    @Enumerated(EnumType.STRING)
    private TypeGestion typeGestion;

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

    public Utilisateur getUtilisateurID() {
        return utilisateurID;
    }

    public void setUtilisateurID(Utilisateur utilisateurID) {
        this.utilisateurID = utilisateurID;
    }

    public Auteur getAuteurID() {
        return auteurID;
    }

    public void setAuteurID(Auteur auteurID) {
        this.auteurID = auteurID;
    }

    public TypeGestion getTypeGestion() {
        return typeGestion;
    }

    public void setTypeGestion(TypeGestion typeGestion) {
        this.typeGestion = typeGestion;
    }
}
