package com.ide.api.entities;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class UtilisateurTagID implements Serializable {
    private Integer utilisateurID;
    private Integer tagID;

    public UtilisateurTagID() {
    }

    public UtilisateurTagID(Integer utilisateurID, Integer tagID) {
        this.utilisateurID = utilisateurID;
        this.tagID = tagID;
    }

    public Integer getUtilisateurID() {
        return utilisateurID;
    }

    public void setUtilisateurID(Integer utilisateurID) {
        this.utilisateurID = utilisateurID;
    }

    public Integer getTagID() {
        return tagID;
    }

    public void setTagID(Integer tagID) {
        this.tagID = tagID;
    }
}
