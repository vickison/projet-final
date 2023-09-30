package com.ide.api.entities;


import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "gestionEtiquette")
public class GestionEtiquette {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PK_GestionEtiquette")
    private Integer id;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "FK_IdAdmin")
    private Admin admin;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "FK_IdEtiquette")
    private Etiquette etiquette;
    private Date dateE;

    public GestionEtiquette() {
    }

    public GestionEtiquette(Admin admin, Etiquette etiquette, Date dateE) {
        this.admin = admin;
        this.etiquette = etiquette;
        this.dateE = dateE;
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


    public Etiquette getEtiquette() {
        return etiquette;
    }

    public void setEtiquette(Etiquette etiquette) {
        this.etiquette = etiquette;
    }

    @Column(name = "dateCreationEtiquette")
    @Temporal(TemporalType.DATE)
    public Date getDateE() {
        return dateE;
    }

    public void setDateE(Date dateE) {
        this.dateE = dateE;
    }
}
