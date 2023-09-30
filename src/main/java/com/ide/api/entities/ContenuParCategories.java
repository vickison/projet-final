package com.ide.api.entities;

import javax.persistence.*;

@Entity
@Table(name = "contenuParCategories")
public class ContenuParCategories {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PK_ContenuParCategories")
    private Integer id;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "FK_IdDocument")
    private Document document;
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
