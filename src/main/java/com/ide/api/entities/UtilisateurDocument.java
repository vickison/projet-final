package com.ide.api.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.ide.api.enums.TypeFichier;
import com.ide.api.enums.TypeGestion;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

@Entity
@DynamicInsert
@IdClass(UtilisateurDocumentID.class)
@Table(name = "tableAdminDocument")
public class UtilisateurDocument implements Serializable {
    @Id
    @ManyToOne
    @JoinColumn(name = "IdentifiantAdmin")
    private Utilisateur utilisateurID;

    @Id
    @ManyToOne
    @JoinColumn(name = "IdentifiantDocument")
    private Document documentID;

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

    public Document getDocument() {
        return documentID;
    }

    public void setDocument(Document document) {
        this.documentID = document;
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

    public Document getDocumentID() {
        return documentID;
    }

    public void setDocumentID(Document documentID) {
        this.documentID = documentID;
    }

    public TypeGestion getTypeGestion() {
        return typeGestion;
    }

    public void setTypeGestion(TypeGestion typeGestion) {
        this.typeGestion = typeGestion;
    }
}
