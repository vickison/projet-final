package com.ide.api.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "etiquettes")
public class Etiquette {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IdEtiquette")
    private Integer IdEtiquette;
    private String etiquette;
    @OneToMany(mappedBy = "etiquette")
    private List<Etiquettage> etiquettages = new ArrayList<>();
    @OneToMany(mappedBy = "etiquette")
    private List<GestionEtiquette> gestionEtiquettes = new ArrayList<>();

    public Etiquette() {
    }

    public Etiquette(String etiquette) {
        this.etiquette = etiquette;
    }


    public Integer getIdEtiquette() {
        return IdEtiquette;
    }

    public void setIdEtiquette(Integer idEtiquette) {
        IdEtiquette = idEtiquette;
    }

    public String getEtiquette() {
        return etiquette;
    }

    public void setEtiquette(String etiquette) {
        this.etiquette = etiquette;
    }


    public List<Etiquettage> getEtiquettages() {
        return etiquettages;
    }

    public void setEtiquettages(List<Etiquettage> etiquettages) {
        this.etiquettages = etiquettages;
    }


    public List<GestionEtiquette> getGestionEtiquettes() {
        return gestionEtiquettes;
    }

    public void setGestionEtiquettes(List<GestionEtiquette> gestionEtiquettes) {
        this.gestionEtiquettes = gestionEtiquettes;
    }
}
