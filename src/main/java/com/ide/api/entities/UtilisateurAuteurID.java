package com.ide.api.entities;

import javax.persistence.Embeddable;
import java.io.Serializable;

public class UtilisateurAuteurID implements Serializable {
    private Integer utilisateurID;
    private Integer auteurID;

    public UtilisateurAuteurID() {
    }

    public UtilisateurAuteurID(Integer utilisateurID, Integer auteurID) {
        this.utilisateurID = utilisateurID;
        this.auteurID = auteurID;
    }

    public Integer getUtilisateurID() {
        return utilisateurID;
    }

    public void setUtilisateurID(Integer utilisateurID) {
        this.utilisateurID = utilisateurID;
    }

    public Integer getAuteurID() {
        return auteurID;
    }

    public void setAuteurID(Integer auteurID) {
        this.auteurID = auteurID;
    }
}
