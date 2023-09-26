package com.ide.api.message;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ide.api.entities.Auteur;
import com.ide.api.entities.Categorie;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.time.Instant;
import java.util.Set;

public class ResponseDocument {
    private Integer id;
    private String titre;
    private String description;
    private Instant date_creation;
    private String type;
    private long size;
    private Set<Categorie> categories;
    private String url;
    private Set<Auteur> auteurs;

    public ResponseDocument(Integer id, String titre, String description, Instant date_creation, String type, long size, Set<Categorie> categories, String url, Set<Auteur> auteurs) {
        this.id = id;
        this.titre = titre;
        this.description = description;
        this.date_creation = date_creation;
        this.type = type;
        this.size = size;
        this.categories = categories;
        this.url = url;
        this.auteurs = auteurs;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Instant getDate_creation() {
        return date_creation;
    }

    public void setDate_creation(Instant date_creation) {
        this.date_creation = date_creation;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public Set<Categorie> getCategories() {
        return categories;
    }

    public void setCategories(Set<Categorie> categories) {
        this.categories = categories;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Set<Auteur> getAuteurs() {
        return auteurs;
    }

    public void setAuteurs(Set<Auteur> auteurs) {
        this.auteurs = auteurs;
    }
}
