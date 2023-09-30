package com.ide.api.entities;

import javax.persistence.*;

@Entity
@Table(name = "etiquettages")
public class Etiquettage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PK_Etiquettages")
    private Integer id;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "FK_IdDocument")
    private Document document;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "FK_IdEtiquette")
    private Etiquette etiquette;

    public Etiquettage() {
    }

    public Etiquettage(Document document, Etiquette etiquette) {
        this.document = document;
        this.etiquette = etiquette;
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


    public Etiquette getEtiquette() {
        return etiquette;
    }

    public void setEtiquette(Etiquette etiquette) {
        this.etiquette = etiquette;
    }
}
