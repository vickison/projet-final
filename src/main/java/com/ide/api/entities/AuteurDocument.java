package com.ide.api.entities;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "auteur_document")
@IdClass(AuteurDocumentID.class)
public class AuteurDocument {
    @Id
    @ManyToOne
    @JoinColumn(name = "auteurID")
    private Auteur auteurID;

    @Id
    @ManyToOne
    @JoinColumn(name = "documentID")
    private Document documentID;
    @Column(name = "date_creation")
    private Date dateCreation;

    @Column(name = "pays_publication")
    private String paysPublication;


    public Auteur getAuteur() {
        return auteurID;
    }

    public void setAuteur(Auteur auteur) {
        this.auteurID = auteur;
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

    public String getPaysPublication() {
        return paysPublication;
    }

    public void setPaysPublication(String paysPublication) {
        this.paysPublication = paysPublication;
    }
}
