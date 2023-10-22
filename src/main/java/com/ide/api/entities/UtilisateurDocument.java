package com.ide.api.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.util.Date;

@Entity
@IdClass(UtilisateurDocumentID.class)
@Table(name = "utilisateur_document")
public class UtilisateurDocument {
    @Id
    @ManyToOne
    @JoinColumn(name = "utilisateurID")
    @JsonBackReference(value = "ut-doc")
    private Utilisateur utilisateurID;

    @Id
    @ManyToOne
    @JoinColumn(name = "documentID")
    @JsonBackReference(value = "doc-ut")
    private Document documentID;

    @Column(name = "date_creation")
    private Date dateCreation;

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

    public Date getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(Date dateCreation) {
        this.dateCreation = dateCreation;
    }
}
