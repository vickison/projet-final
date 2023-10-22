package com.ide.api.entities;

import javax.persistence.Embeddable;
import java.io.Serializable;

public class UtilisateurDocumentID implements Serializable {
    private Integer utilisateurID;
    private Integer documentID;

    public UtilisateurDocumentID() {
    }

    public UtilisateurDocumentID(Integer utilisateurID, Integer documentID) {
        this.utilisateurID = utilisateurID;
        this.documentID = documentID;
    }

    public Integer getUtilisateurID() {
        return utilisateurID;
    }

    public void setUtilisateurID(Integer utilisateurID) {
        this.utilisateurID = utilisateurID;
    }

    public Integer getDocumentID() {
        return documentID;
    }

    public void setDocumentID(Integer documentID) {
        this.documentID = documentID;
    }
}
