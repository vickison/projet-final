package com.ide.api.dto;

import com.ide.api.entities.AuteurDocument;
import com.ide.api.entities.CategorieDocument;
import com.ide.api.entities.DocumentTag;
import com.ide.api.entities.UtilisateurDocument;

import javax.persistence.OneToMany;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class DocumentDTO {
    private Long documentID;
    private String titre;
    private String resume;
    private Date datePublication;
    private byte[] taille;
    private String url;
    private Integer nombreDeTelechargements;
    private Integer nombreDeConsultations;
    private Integer nombreDeCommentaires;
    private String format;
    private String proprietaire;
    private String langue;

    private Set<CategorieDocumentDTO> categorieDocuments = new HashSet<>();
    private Set<AuteurDocumentDTO> auteurDocuments = new HashSet<>();
    private Set<UtilisateurDocumentDTO> utilisateurDocuments = new HashSet<>();
    private Set<DocumentTagDTO> documentTags = new HashSet<>();


    // Getters and setters

    public Long getDocumentID() {
        return documentID;
    }

    public void setDocumentID(Long documentID) {
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

    public Set<CategorieDocumentDTO> getCategorieDocuments() {
        return categorieDocuments;
    }

    public void setCategorieDocuments(Set<CategorieDocumentDTO> categorieDocuments) {
        this.categorieDocuments = categorieDocuments;
    }

    public Set<AuteurDocumentDTO> getAuteurDocuments() {
        return auteurDocuments;
    }

    public void setAuteurDocuments(Set<AuteurDocumentDTO> auteurDocuments) {
        this.auteurDocuments = auteurDocuments;
    }

    public Set<UtilisateurDocumentDTO> getUtilisateurDocuments() {
        return utilisateurDocuments;
    }

    public void setUtilisateurDocuments(Set<UtilisateurDocumentDTO> utilisateurDocuments) {
        this.utilisateurDocuments = utilisateurDocuments;
    }

    public Set<DocumentTagDTO> getDocumentTags() {
        return documentTags;
    }

    public void setDocumentTags(Set<DocumentTagDTO> documentTags) {
        this.documentTags = documentTags;
    }
}
