package com.ide.api.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

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
    @Column(name = "IdAuteur")
    private Integer IdAuteur;
    private String nomAuteur;
    private String prenomAuteur;
    private String emailAuteur;
    private String nationaliteAuteur;
    @OneToMany(mappedBy = "auteur")
    private List<CreationDesContenus> creationDesContenus = new ArrayList<>();
    @OneToMany(mappedBy = "auteur")
    private List<GestionAuteur> gestionAuteurs = new ArrayList<>();

    public Auteur() {
    }

    public Auteur(String nomAuteur, String prenomAuteur, String emailAuteur, String nationaliteAuteur) {
        this.nomAuteur = nomAuteur;
        this.prenomAuteur = prenomAuteur;
        this.emailAuteur = emailAuteur;
        this.nationaliteAuteur = nationaliteAuteur;
    }


    public Integer getIdAuteur() {
        return IdAuteur;
    }

    public void setIdAuteur(Integer idAuteur) {
        IdAuteur = idAuteur;
    }

    public String getNomAuteur() {
        return nomAuteur;
    }

    public void setNomAuteur(String nomAuteur) {
        this.nomAuteur = nomAuteur;
    }

    public String getPrenomAuteur() {
        return prenomAuteur;
    }

    public void setPrenomAuteur(String prenomAuteur) {
        this.prenomAuteur = prenomAuteur;
    }

    public String getEmailAuteur() {
        return emailAuteur;
    }

    public void setEmailAuteur(String emailAuteur) {
        this.emailAuteur = emailAuteur;
    }

    public String getNationaliteAuteur() {
        return nationaliteAuteur;
    }

    public void setNationaliteAuteur(String nationaliteAuteur) {
        this.nationaliteAuteur = nationaliteAuteur;
    }

    public List<CreationDesContenus> getCreationDesContenus() {
        return creationDesContenus;
    }

    public void setCreationDesContenus(List<CreationDesContenus> creationDesContenus) {
        this.creationDesContenus = creationDesContenus;
    }



    public List<GestionAuteur> getGestionAuteurs() {
        return gestionAuteurs;
    }

    public void setGestionAuteurss(List<GestionAuteur> gestionAuteurs) {
        this.gestionAuteurs = gestionAuteurs;
    }

    public void addDocument(CreationDesContenus creationDesContenu){
        this.creationDesContenus.add(creationDesContenu);
    }

    public void addAdmin(GestionAuteur gestionAuteur){
        this.gestionAuteurs.add(gestionAuteur);
    }
}
