package com.ide.api.entities;

import java.io.Serializable;

public class AdminUtilisateurID implements Serializable {
    private Integer adminID;
    private Integer utilisateurID;

    public AdminUtilisateurID() {
    }

    public AdminUtilisateurID(Integer adminID, Integer utilisateurID) {
        this.adminID = adminID;
        this.utilisateurID = utilisateurID;
    }

    public Integer getAdminID() {
        return adminID;
    }

    public void setAdminID(Integer adminID) {
        this.adminID = adminID;
    }

    public Integer getUtilisateurID() {
        return utilisateurID;
    }

    public void setUtilisateurID(Integer utilisateurID) {
        this.utilisateurID = utilisateurID;
    }
}
