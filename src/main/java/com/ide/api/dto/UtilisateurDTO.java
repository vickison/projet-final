package com.ide.api.dto;

import java.util.List;

public class UtilisateurDTO {
    private Integer utilisateurID;

    private String nom;
    private String prenom;
    private String email;

    private String password;

    private boolean admin;

    private List<Integer> categorieIDs;

    private List<Integer> documentIDs;

    private List<Integer> tagIDs;

    private List<Integer> auteurIDs;

    public Integer getUtilisateurID() {
        return utilisateurID;
    }

    public void setUtilisateurID(Integer utilisateurID) {
        this.utilisateurID = utilisateurID;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public List<Integer> getCategorieIDs() {
        return categorieIDs;
    }

    public void setCategorieIDs(List<Integer> categorieIDs) {
        this.categorieIDs = categorieIDs;
    }

    public List<Integer> getDocumentIDs() {
        return documentIDs;
    }

    public void setDocumentIDs(List<Integer> documentIDs) {
        this.documentIDs = documentIDs;
    }

    public List<Integer> getTagIDs() {
        return tagIDs;
    }

    public void setTagIDs(List<Integer> tagIDs) {
        this.tagIDs = tagIDs;
    }

    public List<Integer> getAuteurIDs() {
        return auteurIDs;
    }

    public void setAuteurIDs(List<Integer> auteurIDs) {
        this.auteurIDs = auteurIDs;
    }
}
