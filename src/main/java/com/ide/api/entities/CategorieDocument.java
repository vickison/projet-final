package com.ide.api.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@DynamicInsert
@IdClass(CategorieDocumentID.class)
@Table(name = "tableDocumentCategorie")
public class CategorieDocument implements Serializable {
    @Id
    @ManyToOne
    @JoinColumn(name = "IdentifiantCategorie")
    private  Categorie categorieID;

    @Id
    @ManyToOne
    @JoinColumn(name = "IdentifiantDocument")
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
