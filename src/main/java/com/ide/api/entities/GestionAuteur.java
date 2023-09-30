package com.ide.api.entities;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "gestionAuteur")
public class GestionAuteur {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PK_GestionAuteur")
    private Integer id;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "FK_IdAdmin")
    private Admin admin;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "FK_IdAuteur")
    private Auteur auteur;
    @Column(name = "dateCreationAuteur")
    @Temporal(TemporalType.DATE)
    private Date dateA;

    public GestionAuteur() {
    }

    public GestionAuteur(Admin admin, Auteur auteur, Date dateA) {
        this.admin = admin;
        this.auteur = auteur;
        this.dateA = dateA;
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }


    public Admin getAdmin() {
        return admin;
    }

    public void setAdmin(Admin admin) {
        this.admin = admin;
    }


    public Auteur getAuteur() {
        return auteur;
    }

    public void setAuteur(Auteur auteur) {
        this.auteur = auteur;
    }


    public Date getDateA() {
        return dateA;
    }

    public void setDateA(Date dateA) {
        this.dateA = dateA;
    }
}
