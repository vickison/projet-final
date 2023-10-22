package com.ide.api.entities;

import javax.persistence.Embeddable;
import java.io.Serializable;

public class CategorieDocumentID implements Serializable {
    private Integer categorieID;
    private Integer documentID;

    public CategorieDocumentID() {
    }

    public CategorieDocumentID(Integer categorieID, Integer documentID) {
        this.categorieID = categorieID;
        this.documentID = documentID;
    }

    public Integer getCategorieID() {
        return categorieID;
    }

    public void setCategorieID(Integer categorieID) {
        this.categorieID = categorieID;
    }

    public Integer getDocumentID() {
        return documentID;
    }

    public void setDocumentID(Integer documentID) {
        this.documentID = documentID;
    }
}
