package com.ide.api.message;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ide.api.entities.Categorie;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.time.Instant;

public class ResponseDocument {
    private Integer id;
    private String titre;
    private String description;
    private Instant date_creation;
    private String type;
    private long size;
    private Categorie categorie;

    public ResponseDocument(Integer id, String titre, String description, Instant date_creation, String type, long size, Categorie categorie) {
        this.id = id;
        this.titre = titre;
        this.description = description;
        this.date_creation = date_creation;
        this.type = type;
        this.size = size;
        this.categorie = categorie;
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

    public Categorie getCategorie() {
        return categorie;
    }

    public void setCategorie(Categorie categorie) {
        this.categorie = categorie;
    }
}
