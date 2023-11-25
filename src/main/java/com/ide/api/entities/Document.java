package com.ide.api.entities;

import com.fasterxml.jackson.annotation.*;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.persistence.Entity;
import java.util.*;



@EntityListeners(AuditingEntityListener.class)
@Entity
@Table(name = "document")
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "documentID")
public class Document {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "documentID")
    private Integer documentID;

    private String titre;

    //Une briève description du document
    @Lob
    @Column(name = "resume")
    private String resume;


    @Column(name = "date_publication")
    private Date datePublication;

    @Lob
    @Column(name = "taille")
    private byte[] taille;

    private String url;
    @Column(name = "nb_telechargements", columnDefinition = "Integer default '0'")
    private Integer nombreDeTelechargements;
    @Column(name = "nb_consultations", columnDefinition = "Integer default '0'")
    private Integer nombreDeConsultations;

    @Column(name = "nb_commentaires", columnDefinition = "Integer default '0'")
    private Integer nombreDeCommentaires;
    private String format;
    private String proprietaire;
    private String langue;

    @OneToMany(mappedBy = "documentID")
    @JsonIgnore
    private Set<CategorieDocument> categorieDocuments = new HashSet<>();

    @OneToMany(mappedBy = "documentID")
    private Set<AuteurDocument> auteurDocuments = new HashSet<>();

    @OneToMany(mappedBy = "documentID")
    @JsonIgnore
    private Set<UtilisateurDocument> utilisateurDocuments = new HashSet<>();

    @OneToMany(mappedBy = "documentID")
    private Set<DocumentTag> documentTags = new HashSet<>();


    public Integer getDocumentID() {
        return documentID;
    }

    public void setDocumentID(Integer documentID) {
        this.documentID = documentID;
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

    public Date getDatePublication() {
        return datePublication;
    }

    public void setDatePublication(Date datePublication) {
        this.datePublication = datePublication;
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

    public Integer getNombreDeCommentaires() {
        return nombreDeCommentaires;
    }

    public void setNombreDeCommentaires(Integer nombreDeCommentaires) {
        this.nombreDeCommentaires = nombreDeCommentaires;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
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

    public Set<CategorieDocument> getCategorieDocuments() {
        return categorieDocuments;
    }

    public void setCategorieDocuments(Set<CategorieDocument> categorieDocuments) {
        this.categorieDocuments = categorieDocuments;
    }

    public Set<AuteurDocument> getAuteurDocuments() {
        return auteurDocuments;
    }

    public void setAuteurDocuments(Set<AuteurDocument> auteurDocuments) {
        this.auteurDocuments = auteurDocuments;
    }

    public Set<UtilisateurDocument> getUtilisateurDocuments() {
        return utilisateurDocuments;
    }

    public void setUtilisateurDocuments(Set<UtilisateurDocument> utilisateurDocuments) {
        this.utilisateurDocuments = utilisateurDocuments;
    }

    public Set<DocumentTag> getDocumentTags() {
        return documentTags;
    }

    public void setDocumentTags(Set<DocumentTag> documentTags) {
        this.documentTags = documentTags;
    }
}
