package com.ide.api.dto;

import com.ide.api.entities.Document;
import com.ide.api.entities.Utilisateur;

import java.util.List;

public class CategorieDTO {
    private int categorieID;
    private String nom;

    private List<Integer> utlisateurIds;
    private List<String> utilisateurNames;

    private List<Integer> documentIds;
    private List<String> documentNames;

    public int getCategorieID() {
        return categorieID;
    }

    public void setCategorieID(int categorieID) {
        this.categorieID = categorieID;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public List<Integer> getUtlisateurIds() {
        return utlisateurIds;
    }

    public void setUtlisateurIds(List<Integer> utlisateurIds) {
        this.utlisateurIds = utlisateurIds;
    }

    public List<String> getUtilisateurNames() {
        return utilisateurNames;
    }

    public void setUtilisateurNames(List<String> utilisateurNames) {
        this.utilisateurNames = utilisateurNames;
    }

    public List<Integer> getDocumentIds() {
        return documentIds;
    }

    public void setDocumentIds(List<Integer> documentIds) {
        this.documentIds = documentIds;
    }

    public List<String> getDocumentNames() {
        return documentNames;
    }

    public void setDocumentNames(List<String> documentNames) {
        this.documentNames = documentNames;
    }
}
