package com.ide.api.entities;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.*;


@Entity
@Table(name = "categories")

public class Categorie implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IdCategorie")
    private int IdCategorie;
    private String nomCategorie;
    @JsonIgnoreProperties("categorie")
    @OneToMany(mappedBy = "categorie")
    private List<ContenuParCategories> contenuParCategories = new ArrayList<>();

    @JsonIgnoreProperties("categorie")
    @OneToMany(mappedBy = "categorie")
    private List<GestionCategorie> gestionCategories = new ArrayList<>();

    public Categorie() {
    }

    public Categorie(String nomCategorie,
                     List<ContenuParCategories> contenuParCategories,
                     List<GestionCategorie> gestionCategories) {
        this.nomCategorie = nomCategorie;
        this.contenuParCategories = contenuParCategories;
        this.gestionCategories = gestionCategories;
    }



    public int getIdCategorie() {
        return IdCategorie;
    }

    public void setIdCategorie(int idCategorie) {
        IdCategorie = idCategorie;
    }

    public String getNomCategorie() {
        return nomCategorie;
    }

    public void setNomCategorie(String nomCategorie) {
        this.nomCategorie = nomCategorie;
    }

    public List<ContenuParCategories> getContenuParCategories() {
        return contenuParCategories;
    }


    public void setContenuParCategories(List<ContenuParCategories> contenuParCategories) {
        this.contenuParCategories = contenuParCategories;
    }


    public List<GestionCategorie> getGestionCategories() {
        return gestionCategories;
    }

    public void setGestionCategories(List<GestionCategorie> gestionCategories) {
        this.gestionCategories = gestionCategories;
    }

    public void addDocument(ContenuParCategories contenuParCategorie) {
        this.contenuParCategories.add(contenuParCategorie);
    }

    public void addAdmin(GestionCategorie gestionCategorie) {
        this.gestionCategories.add(gestionCategorie);
    }

}

