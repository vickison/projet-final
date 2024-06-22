package com.ide.api.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "tableAuteurs")
public class Auteur{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idAuteur")
    private Integer auteurID;
    @Column(name = "NOMAuteur")
    private String nom;
    @Column(name = "PRENOMAuteur")
    private String prenom;
    @Column(name = "EMAILAuteur")
    private String email;

    private Timestamp DateCreationAuteur;
    private String AuteurCreationAuteur;
    @UpdateTimestamp
    private Timestamp DateModificationAuteur;
    private String AuteurModificationAuteur;
    private boolean SupprimerAuteur;

    @OneToMany(mappedBy = "auteurID")
    @JsonIgnore
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

    public Timestamp getDateCreationAuteur() {
        return DateCreationAuteur;
    }

    public void setDateCreationAuteur(Timestamp dateCreationAuteur) {
        DateCreationAuteur = dateCreationAuteur;
    }

    public String getAuteurCreationAuteur() {
        return AuteurCreationAuteur;
    }

    public void setAuteurCreationAuteur(String auteurCreationAuteur) {
        AuteurCreationAuteur = auteurCreationAuteur;
    }

    public Timestamp getDateModificationAuteur() {
        return DateModificationAuteur;
    }

    public void setDateModificationAuteur(Timestamp dateModificationAuteur) {
        DateModificationAuteur = dateModificationAuteur;
    }

    public String getAuteurModificationAuteur() {
        return AuteurModificationAuteur;
    }

    public void setAuteurModificationAuteur(String auteurModificationAuteur) {
        AuteurModificationAuteur = auteurModificationAuteur;
    }

    public boolean getSupprimerAuteur() {
        return SupprimerAuteur;
    }

    public void setSupprimerAuteur(boolean supprimerAuteur) {
        SupprimerAuteur = supprimerAuteur;
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
