package com.ide.api.entities;

import javax.persistence.Embeddable;
import java.io.Serializable;


public class UtilisateurCategorieID implements Serializable {
    private Integer utilisateurID;
    private Integer categorieID;

    public UtilisateurCategorieID() {
    }

    public UtilisateurCategorieID(Integer utilisateurID, Integer categorieID) {
        this.utilisateurID = utilisateurID;
        this.categorieID = categorieID;
    }

    public Integer getUtilisateurID() {
        return utilisateurID;
    }

    public void setUtilisateurID(Integer utilisateurID) {
        this.utilisateurID = utilisateurID;
    }

    public Integer getCategorieID() {
        return categorieID;
    }

    public void setCategorieID(Integer categorieID) {
        this.categorieID = categorieID;
    }
}
