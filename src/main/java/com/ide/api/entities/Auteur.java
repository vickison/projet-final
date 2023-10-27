package com.ide.api.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "auteurs")
public class Auteur{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer auteurID;
    private String nom;
    private String prenom;
    private String email;
    private String nationalite;

    @OneToMany(mappedBy = "auteurID")
    private Set<AuteurDocument> auteurDocuments = new HashSet<>();

    @OneToMany(mappedBy = "auteurID")
    @JsonIgnore
    private Set<UtilisateurAuteur> utilisateurAuteurs = new HashSet<>();

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

    public Set<AuteurDocument> getAuteurDocuments() {
        return auteurDocuments;
    }

    public void setAuteurDocuments(Set<AuteurDocument> auteurDocuments) {
        this.auteurDocuments = auteurDocuments;
    }

    public Set<UtilisateurAuteur> getUtilisateurAuteurs() {
        return utilisateurAuteurs;
    }

    public void setUtilisateurAuteurs(Set<UtilisateurAuteur> utilisateurAuteurs) {
        this.utilisateurAuteurs = utilisateurAuteurs;
    }
}
