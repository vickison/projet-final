package com.ide.api.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.util.Date;

@Entity
@IdClass(UtilisateurTagID.class)
@Table(name = "utilisateur_tag")
public class UtilisateurTag {
    @Id
    @ManyToOne
    @JoinColumn(name = "utilisateurID")
    private Utilisateur utilisateurID;

    @Id
    @ManyToOne
    @JoinColumn(name = "tagID")
    private Tag tagID;

    @Column(name = "date_creation")
    private Date dateCreation;

    public Utilisateur getUtilisateur() {
        return utilisateurID;
    }

    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateurID = utilisateur;
    }

    public Tag getTag() {
        return tagID;
    }

    public void setTag(Tag tag) {
        this.tagID = tag;
    }

    public Date getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(Date dateCreation) {
        this.dateCreation = dateCreation;
    }
}
