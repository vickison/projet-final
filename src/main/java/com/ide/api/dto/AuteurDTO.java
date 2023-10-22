package com.ide.api.dto;

public class AuteurDTO {
    private Integer auteurID;
    private String nom;
    private String prenom;
    private String email;
    private String nationalite;

    public Integer getAuteurID() {
        return auteurID;
    }

    public void setAuteurID(Integer auteurID) {
        this.auteurID = auteurID;
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

    public String getNationalite() {
        return nationalite;
    }

    public void setNationalite(String nationalite) {
        this.nationalite = nationalite;
    }
}
