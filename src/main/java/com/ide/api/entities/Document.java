package com.ide.api.entities;

import com.fasterxml.jackson.annotation.*;
import com.ide.api.enums.Langue;
import com.ide.api.enums.TypeFichier;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.persistence.Entity;
import java.sql.Timestamp;
import java.time.Year;
import java.util.*;



@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "tableDocuments")
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "documentID")
public class Document {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idDocument")
    private Integer documentID;
    @Column(name = "TITRE")
    private String titre;
    @Column(name = "DESCRIPTION", columnDefinition = "TEXT")
    private String resume;
    @Column(name = "TAILLE")
    private long taille;
    @Column(name = "FICHIER")
    private String url;
    @Column(name = "NombreDeConsultations")
    private Double nombreDeConsultations;
    @Column(name = "NombreDePartages")
    private Double NombreDePartages;
    @Column(name = "TypeContenu")
    private String format;
    @Column(name = "TypeFichier")
    @Enumerated(EnumType.STRING)
    private TypeFichier typeFichier;
    private Float NOTE;
    private Double NombreNotes;
    @Column(name = "LANGUE")
    @Enumerated(EnumType.STRING)
    private Langue langue;
    private Year AnneePublication;
    @Column(name = "DateCreationDocument")
    private Timestamp dateCreationDocument;
    private String AuteurCreationDocument;
    @UpdateTimestamp
    private Timestamp DateModificationDocument;
    private String AuteurModificationDocument;
    private boolean SupprimerDocument;

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

    public long getTaille() {
        return taille;
    }

    public void setTaille(long taille) {
        this.taille = taille;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Double getNombreDeConsultations() {
        return nombreDeConsultations;
    }

    public void setNombreDeConsultations(Double nombreDeConsultations) {
        this.nombreDeConsultations = nombreDeConsultations;
    }

    public Double getNombreDePartages() {
        return NombreDePartages;
    }

    public void setNombreDePartages(Double nombreDePartages) {
        NombreDePartages = nombreDePartages;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public Langue getLangue() {
        return langue;
    }

    public void setLangue(Langue langue) {
        this.langue = langue;
    }

    public Year getAnneePublication() {
        return AnneePublication;
    }

    public void setAnneePublication(Year anneePublication) {
        AnneePublication = anneePublication;
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


    public Timestamp getDateCreationDocument() {
        return dateCreationDocument;
    }

    public void setDateCreationDocument(Timestamp dateCreationDocument) {
        this.dateCreationDocument = dateCreationDocument;
    }

    public String getAuteurCreationDocument() {
        return AuteurCreationDocument;
    }

    public void setAuteurCreationDocument(String auteurCreationDocument) {
        AuteurCreationDocument = auteurCreationDocument;
    }

    public Timestamp getDateModificationDocument() {
        return DateModificationDocument;
    }

    public void setDateModificationDocument(Timestamp dateModificationDocument) {
        DateModificationDocument = dateModificationDocument;
    }

    public String getAuteurModificationDocument() {
        return AuteurModificationDocument;
    }

    public void setAuteurModificationDocument(String auteurModificationDocument) {
        AuteurModificationDocument = auteurModificationDocument;
    }

    public boolean isSupprimerDocument() {
        return SupprimerDocument;
    }

    public void setSupprimerDocument(boolean supprimerDocument) {
        SupprimerDocument = supprimerDocument;
    }

    public Float getNOTE() {
        return NOTE;
    }

    public void setNOTE(Float NOTE) {
        this.NOTE = NOTE;
    }

    public Double getNombreNotes() {
        return NombreNotes;
    }

    public void setNombreNotes(Double nombreNotes) {
        NombreNotes = nombreNotes;
    }

    public TypeFichier getTypeFichier() {
        return typeFichier;
    }

    public void setTypeFichier(TypeFichier typeFichier) {
        this.typeFichier = typeFichier;
    }
}

