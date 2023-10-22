package com.ide.api.dto;

import java.util.Date;

public class AuteurDocumentDTO {
    private Long auteurId;
    private Long documentId;
    private Date dateCreation;
    private String paysPublication;

    public AuteurDocumentDTO() {
    }

    public AuteurDocumentDTO(Long auteurId,
                             Long documentId,
                             Date dateCreation,
                             String paysPublication) {
        this.auteurId = auteurId;
        this.documentId = documentId;
        this.dateCreation = dateCreation;
        this.paysPublication = paysPublication;
    }

    public Long getAuteurId() {
        return auteurId;
    }

    public void setAuteurId(Long auteurId) {
        this.auteurId = auteurId;
    }

    public Long getDocumentId() {
        return documentId;
    }

    public void setDocumentId(Long documentId) {
        this.documentId = documentId;
    }

    public Date getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(Date dateCreation) {
        this.dateCreation = dateCreation;
    }

    public String getPaysPublication() {
        return paysPublication;
    }

    public void setPaysPublication(String paysPublication) {
        this.paysPublication = paysPublication;
    }
}
