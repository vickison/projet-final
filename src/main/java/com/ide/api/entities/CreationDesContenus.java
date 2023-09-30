package com.ide.api.entities;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "creationDesContenus")
public class CreationDesContenus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PK_GestionParCategorie")
    private Integer id;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "FK_IdDocument")
    private Document document;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "FK_IdAuteur")
    private Auteur auteur;

    private Date dateCreation;

    private String paysPublication;

    public CreationDesContenus() {
    }

    public CreationDesContenus(Document document, Auteur auteur, Date dateCreation, String paysPublication) {
        this.document = document;
        this.auteur = auteur;
        this.dateCreation = dateCreation;
        this.paysPublication = paysPublication;
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


    public Auteur getAuteur() {
        return auteur;
    }

    public void setAuteur(Auteur auteur) {
        this.auteur = auteur;
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
