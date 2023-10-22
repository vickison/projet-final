package com.ide.api.entities;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class DocumentTagID implements Serializable {
    private Integer documentID;
    private Integer tagID;

    public DocumentTagID() {
    }

    public DocumentTagID(Integer documentID, Integer tagID) {
        this.documentID = documentID;
        this.tagID = tagID;
    }

    public Integer getDocumentID() {
        return documentID;
    }

    public void setDocumentID(Integer documentID) {
        this.documentID = documentID;
    }

    public Integer getTagID() {
        return tagID;
    }

    public void setTagID(Integer tagID) {
        this.tagID = tagID;
    }
}
