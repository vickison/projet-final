package com.ide.api.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "admins")
public class Admin implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IdAdmin")
    private Integer IdAdmin;
    private String nomAdmin;
    private String prenomAdmin;
    private String emailAdmin;
    private String motDePasse;

    @JsonIgnoreProperties("admin")
    @OneToMany(mappedBy = "admin")
    private List<GestionContenus> gestionContenus = new ArrayList<>();
    @JsonIgnoreProperties("admin")
    @OneToMany(mappedBy = "admin")
    private List<GestionCategorie> gestionCategories = new ArrayList<>();
    @JsonIgnoreProperties("admin")
    @OneToMany(mappedBy = "admin")
    private List<GestionEtiquette> gestionEtiquettes = new ArrayList<>();

    @JsonIgnoreProperties("admin")
    @OneToMany(mappedBy = "admin")
    private List<GestionAuteur> gestionAuteurs = new ArrayList<>();

    public Admin() {
    }

    public Admin(String nomAdmin, String prenomAdmin,
                 String emailAdmin, String motDePasse,
                 List<GestionContenus> gestionContenus,
                 List<GestionCategorie> gestionCategories,
                 List<GestionEtiquette> gestionEtiquettes,
                 List<GestionAuteur> gestionAuteurs) {
        this.nomAdmin = nomAdmin;
        this.prenomAdmin = prenomAdmin;
        this.emailAdmin = emailAdmin;
        this.motDePasse = motDePasse;
        this.gestionContenus = gestionContenus;
        this.gestionCategories = gestionCategories;
        this.gestionEtiquettes = gestionEtiquettes;
        this.gestionAuteurs = gestionAuteurs;
    }

    public Integer getIdAdmin() {
        return IdAdmin;
    }

    public void setIdAdmin(Integer idAdmin) {
        IdAdmin = idAdmin;
    }

    public String getNomAdmin() {
        return nomAdmin;
    }

    public void setNomAdmin(String nomAdmin) {
        this.nomAdmin = nomAdmin;
    }

    public String getPrenomAdmin() {
        return prenomAdmin;
    }

    public void setPrenomAdmin(String prenomAdmin) {
        this.prenomAdmin = prenomAdmin;
    }

    public String getEmailAdmin() {
        return emailAdmin;
    }

    public void setEmailAdmin(String emailAdmin) {
        this.emailAdmin = emailAdmin;
    }

    public String getMotDePasse() {
        return motDePasse;
    }

    public void setMotDePasse(String motDePasse) {
        this.motDePasse = motDePasse;
    }

    public List<GestionContenus> getGestionContenus() {
        return gestionContenus;
    }

    public void setGestionContenus(List<GestionContenus> gestionContenus) {
        this.gestionContenus = gestionContenus;
    }


    public List<GestionCategorie> getGestionCategories() {
        return gestionCategories;
    }

    public void setGestionCategories(List<GestionCategorie> gestionCategories) {
        this.gestionCategories = gestionCategories;
    }


    public List<GestionEtiquette> getGestionEtiquettes() {
        return gestionEtiquettes;
    }

    public void setGestionEtiquettes(List<GestionEtiquette> gestionEtiquettes) {
        this.gestionEtiquettes = gestionEtiquettes;
    }


    public List<GestionAuteur> getGestionAuteurs() {
        return gestionAuteurs;
    }

    public void setGestionAuteurs(List<GestionAuteur> gestionAuteurs) {
        this.gestionAuteurs = gestionAuteurs;
    }

    public void addEtiquette(GestionEtiquette gestionEtiquette) {
        this.gestionEtiquettes.add(gestionEtiquette);
    }

    public void addDocument(GestionContenus gestionContenu) {
        this.gestionContenus.add(gestionContenu);
    }

    public void addCategorie(GestionCategorie gestionCategorie){
        this.gestionCategories.add(gestionCategorie);
    }

}
