package com.ide.api.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "gestionCategories")

public class GestionCategorie implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PK_GestionContenu")
    private Integer id;
    @JsonIgnoreProperties("gestionCategories")
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "FK_IdAdmin")
    private Admin admin;
    @JsonIgnoreProperties("gestionCategories")
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "FK_IdCategorie")
    private Categorie categorie;

    @Column(name = "dateCreationCategorie")
    private LocalDateTime dateCat;

    public GestionCategorie() {
    }

    public GestionCategorie(Admin admin, Categorie categorie, LocalDateTime dateCat) {
        this.admin = admin;
        this.categorie = categorie;
        this.dateCat = dateCat;
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


    public Categorie getCategorie() {
        return categorie;
    }

    public void setCategorie(Categorie categorie) {
        this.categorie = categorie;
    }


    public LocalDateTime getDateCat() {
        return dateCat;
    }


    public void setDateCat(LocalDateTime dateCat) {
        this.dateCat = dateCat;
    }
}
