package com.ide.api.entities;

import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@DynamicInsert
@Table(name = "tableDocumentAuteur")
@IdClass(AuteurDocumentID.class)
public class AuteurDocument implements Serializable {
    @Id
    @ManyToOne
    @JoinColumn(name = "IdentifiantAuteur")
    private Auteur auteurID;

    @Id
    @ManyToOne
    @JoinColumn(name = "IdentifiantDocument")
    private Document documentID;


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

}
