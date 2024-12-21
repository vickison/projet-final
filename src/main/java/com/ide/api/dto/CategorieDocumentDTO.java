package com.ide.api.dto;

import java.util.Date;

public class CategorieDocumentDTO {
    private Integer categorieId;
    private Integer documentId;
    private String categorie;
    private String titre;
    private String fichier;
    private String typeContenu;
    private String description;
    private String thumbnail;

    public CategorieDocumentDTO(Integer categorieId, Integer documentId, String categorie, String titre, String fichier, String typeContenu, String description, String thumbnail) {
        this.categorieId = categorieId;
        this.documentId = documentId;
        this.categorie = categorie;
        this.titre = titre;
        this.fichier = fichier;
        this.typeContenu = typeContenu;
        this.description = description;
        this.thumbnail = thumbnail;
    }

    public Integer getCategorieId() {
        return categorieId;
    }

    public void setCategorieId(Integer categorieId) {
        this.categorieId = categorieId;
    }

    public Integer getDocumentId() {
        return documentId;
    }

    public void setDocumentId(Integer documentId) {
        this.documentId = documentId;
    }

    public String getCategorie() {
        return categorie;
    }

    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getFichier() {
        return fichier;
    }

    public void setFichier(String fichier) {
        this.fichier = fichier;
    }

    public String getTypeContenu() {
        return typeContenu;
    }

    public void setTypeContenu(String typeContenu) {
        this.typeContenu = typeContenu;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }
}
