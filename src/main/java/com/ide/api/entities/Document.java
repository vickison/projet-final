package com.ide.api.entities;

import com.fasterxml.jackson.annotation.*;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.NaturalId;
import org.hibernate.annotations.NaturalIdCache;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.persistence.Entity;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;


/*

On definit ici une classe Document, qui est une entité,
donc qui va nous créer, grace à des annotations, la table documents dans la base avec les champs nécéssaires

* */
@EntityListeners(AuditingEntityListener.class)
@Entity
@Table(name = "documents")

public class Document {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IdDocument")
    private Integer IdDocument;

    private String titre;

    //Une briève description du document
    @Lob
    @Column(name = "resume")
    private String resume;


    //La date d'ajout du document

    private Date dateEnregistrement = new Date();

    @Lob
    @Column(name = "taille")
    private byte[] taille;

    private String url;
    private Integer nombreDeTelechargements;
    private Integer nombreDeConsultations;
    private String formatDocument;
    private String proprietaire;
    private String langue;

    @JsonIgnore
    @OneToMany(mappedBy = "document")
    private List<ContenuParCategories> contenuParCategories = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "document")
    private List<CreationDesContenus> creationDesContenus = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "document")
    private List<Etiquettage> etiquettages = new ArrayList<>();
    @JsonIgnore
    @OneToMany(mappedBy = "document")
    private List<GestionContenus> gestionContenus = new ArrayList<>();
    private Double note;
    private Integer nombreCommentaires;

    public Document() {
    }

    public Document(String titre,
                    String resume,
                    Date dateEnregistrement,
                    byte[] taille, String url,
                    Integer nombreDeTelechargements,
                    Integer nombreDeConsultations,
                    String formatDocument,
                    String proprietaire,
                    String langue,
                    Double note,
                    Integer nombreCommentaires) {
        this.titre = titre;
        this.resume = resume;
        this.dateEnregistrement = dateEnregistrement;
        this.taille = taille;
        this.url = url;
        this.nombreDeTelechargements = nombreDeTelechargements;
        this.nombreDeConsultations = nombreDeConsultations;
        this.formatDocument = formatDocument;
        this.proprietaire = proprietaire;
        this.langue = langue;
        this.note = note;
        this.nombreCommentaires = nombreCommentaires;
    }


    public Integer getIdDocument() {
        return IdDocument;
    }

    public void setIdDocument(Integer idDocument) {
        IdDocument = idDocument;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }


    public String getResume() {
        return resume;
    }

    public void setResume(String resume) {
        this.resume = resume;
    }

    public Date getDateEnregistrement() {
        return dateEnregistrement;
    }

    public void setDateEnregistrement(Date dateEnregistrement) {
        this.dateEnregistrement = dateEnregistrement;
    }


    public byte[] getTaille() {
        return taille;
    }

    public void setTaille(byte[] taille) {
        this.taille = taille;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getNombreDeTelechargements() {
        return nombreDeTelechargements;
    }

    public void setNombreDeTelechargements(Integer nombreDeTelechargements) {
        this.nombreDeTelechargements = nombreDeTelechargements;
    }

    public Integer getNombreDeConsultations() {
        return nombreDeConsultations;
    }

    public void setNombreDeConsultations(Integer nombreDeConsultations) {
        this.nombreDeConsultations = nombreDeConsultations;
    }

    public String getFormatDocument() {
        return formatDocument;
    }

    public void setFormatDocument(String formatDocument) {
        this.formatDocument = formatDocument;
    }

    public String getProprietaire() {
        return proprietaire;
    }

    public void setProprietaire(String proprietaire) {
        this.proprietaire = proprietaire;
    }

    public String getLangue() {
        return langue;
    }

    public void setLangue(String langue) {
        this.langue = langue;
    }


    public List<ContenuParCategories> getContenuParCategories() {
        return contenuParCategories;
    }

    public void setContenuParCategories(List<ContenuParCategories> contenuParCategories) {
        this.contenuParCategories = contenuParCategories;
    }


    public List<CreationDesContenus> getCreationDesContenus() {
        return creationDesContenus;
    }

    public void setCreationDesContenus(List<CreationDesContenus> creationDesContenus) {
        this.creationDesContenus = creationDesContenus;
    }


    public List<Etiquettage> getEtiquettages() {
        return etiquettages;
    }

    public void setEtiquettages(List<Etiquettage> etiquettages) {
        this.etiquettages = etiquettages;
    }


    public List<GestionContenus> getGestionContenus() {
        return gestionContenus;
    }

    public void setGestionContenus(List<GestionContenus> gestionContenus) {
        this.gestionContenus = gestionContenus;
    }

    public Double getNote() {
        return note;
    }

    public void setNote(Double note) {
        this.note = note;
    }

    public Integer getNombreCommentaires() {
        return nombreCommentaires;
    }

    public void setNombreCommentaires(Integer nombreCommentaires) {
        this.nombreCommentaires = nombreCommentaires;
    }

    public void addCategorie(ContenuParCategories contenuParCategorie) {
        this.contenuParCategories.add(contenuParCategorie);
    }
}
