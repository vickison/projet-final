package com.ide.api.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.ide.api.enums.TypeGestion;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

@Entity
@DynamicInsert
@IdClass(UtilisateurTagID.class)
@Table(name = "tableAdminEtiquette")
public class UtilisateurTag implements Serializable {
    @Id
    @ManyToOne
    @JoinColumn(name = "IdentifiantAdmin")
    private Utilisateur utilisateurID;

    @Id
    @ManyToOne
    @JoinColumn(name = "IdentifiantEtiquette")
    private Tag tagID;

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

    public Tag getTag() {
        return tagID;
    }

    public void setTag(Tag tag) {
        this.tagID = tag;
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

    public Tag getTagID() {
        return tagID;
    }

    public void setTagID(Tag tagID) {
        this.tagID = tagID;
    }

    public TypeGestion getTypeGestion() {
        return typeGestion;
    }

    public void setTypeGestion(TypeGestion typeGestion) {
        this.typeGestion = typeGestion;
    }
}
