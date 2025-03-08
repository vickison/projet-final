package com.ide.api.entities;

import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.IdClass;
import java.io.Serializable;
import java.util.Objects;


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


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AuteurDocumentID that = (AuteurDocumentID) o;
        return Objects.equals(auteurID, that.auteurID) && Objects.equals(documentID, that.documentID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(auteurID, documentID);
    }
}
