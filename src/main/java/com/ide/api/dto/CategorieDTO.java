package com.ide.api.dto;

import com.ide.api.entities.Document;
import com.ide.api.entities.Utilisateur;

import java.util.List;

public class CategorieDTO {
    private String nom;

    public CategorieDTO() {
    }

    public CategorieDTO(String nom) {
        this.nom = nom;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }
}
