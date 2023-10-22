package com.ide.api.dto;

import java.util.Date;

public class UtilisateurDocumentDTO {
    private Long utilisateurId;
    private Long documentId;

    public UtilisateurDocumentDTO() {
    }

    public UtilisateurDocumentDTO(Long utilisateurId, Long documentId) {
        this.utilisateurId = utilisateurId;
        this.documentId = documentId;
    }

    public Long getUtilisateurId() {
        return utilisateurId;
    }

    public void setUtilisateurId(Long utilisateurId) {
        this.utilisateurId = utilisateurId;
    }

    public Long getDocumentId() {
        return documentId;
    }

    public void setDocumentId(Long documentId) {
        this.documentId = documentId;
    }

}
