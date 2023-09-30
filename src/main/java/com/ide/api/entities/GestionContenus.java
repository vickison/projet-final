package com.ide.api.entities;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "gestionsContenus")
public class GestionContenus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PK_GestionContenus")
    private Integer id;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "FK_IdAdmin")
    private Admin admin;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "FK_IdDocument")
    private Document document;
    private Date dateC;

    public GestionContenus() {
    }

    public GestionContenus(Admin admin, Document document, Date dateC) {
        this.admin = admin;
        this.document = document;
        this.dateC = dateC;
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }


    public Admin getAdmin() {
        return admin;
    }

    public void setAdmin(Admin admin) {
        this.admin = admin;
    }


    public Document getDocument() {
        return document;
    }

    public void setDocument(Document document) {
        this.document = document;
    }


    public Date getDateC() {
        return dateC;
    }

    public void setDateC(Date dateC) {
        this.dateC = dateC;
    }
}
