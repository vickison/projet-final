package com.ide.api.entities;

import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.IdClass;
import java.io.Serializable;


public class AuteurDocumentID implements Serializable {
    private Integer auteurID;
    private Integer documentID;

    public AuteurDocumentID() {
    }

    public AuteurDocumentID(Integer auteurID, Integer documentID) {
        this.auteurID = auteurID;
        this.documentID = documentID;
    }

    public Integer getAuteurID() {
        return auteurID;
    }

    public void setAuteurID(Integer auteurID) {
        this.auteurID = auteurID;
    }

    public Integer getDocumentID() {
        return documentID;
    }

    public void setDocumentID(Integer documentID) {
        this.documentID = documentID;
    }
}
