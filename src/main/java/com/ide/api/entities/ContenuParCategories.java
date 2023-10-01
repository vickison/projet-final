package com.ide.api.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

@Entity
@Table(name = "contenuParCategories")
public class ContenuParCategories {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PK_ContenuParCategories")
    private Integer id;
    @JsonIgnoreProperties("contenuParCategories")
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "FK_IdDocument")
    private Document document;
    @JsonIgnoreProperties("contenuParCategories")
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "FK_IdCategorie")
    private Categorie categorie;

    public ContenuParCategories() {
    }

    public ContenuParCategories(Document document, Categorie categorie) {
        this.document = document;
        this.categorie = categorie;
    }


    public Integer getId() {
        return id;
    }


    public void setId(Integer id) {
        this.id = id;
    }



    public Document getDocument() {
        return document;
    }

    public void setDocument(Document document) {
        this.document = document;
    }



    public Categorie getCategorie() {
        return categorie;
    }

    public void setCategorie(Categorie categorie) {
        this.categorie = categorie;
    }
}
