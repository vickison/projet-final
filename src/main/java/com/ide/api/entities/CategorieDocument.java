package com.ide.api.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;

@Entity
@IdClass(CategorieDocumentID.class)
@Table(name = "categorie_document")
public class CategorieDocument {
    @Id
    @ManyToOne
    @JoinColumn(name = "categorieID")
    @JsonBackReference(value = "cat-doc")
    private  Categorie categorieID;

    @Id
    @ManyToOne
    @JoinColumn(name = "documentID")
    @JsonBackReference(value = "doc-cat")
    private Document documentID;

    public Categorie getCategorie() {
        return categorieID;
    }

    public void setCategorie(Categorie categorie) {
        this.categorieID = categorie;
    }

    public Document getDocument() {
        return documentID;
    }

    public void setDocument(Document document) {
        this.documentID = document;
    }
}
