package com.ide.api.entities;

import com.ide.api.enums.TypeGestion;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@DynamicInsert
@Table(name = "tableAdminUtilisateur")
@IdClass(AdminUtilisateurID.class)
public class AdminUtilisateur implements Serializable {
    @Id
    @ManyToOne
    @JoinColumn(name = "IdentifiantAdmin")
    private Utilisateur adminID;
    @Id
    @JoinColumn(name = "IdentifiantUtilisateur")
    @ManyToOne
    private Utilisateur utilisateurID;
    @Column(name = "DATE")
    private Date dateCreation;
    @Column(name = "TypeGestion")
    @Enumerated(EnumType.STRING)
    private TypeGestion typeGestion;


    public Utilisateur getAdminID() {
        return adminID;
    }

    public void setAdminID(Utilisateur adminID) {
        this.adminID = adminID;
    }

    public Utilisateur getUtilisateurID() {
        return utilisateurID;
    }

    public void setUtilisateurID(Utilisateur utilisateurID) {
        this.utilisateurID = utilisateurID;
    }

    public Date getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(Date dateCreation) {
        this.dateCreation = dateCreation;
    }

    public TypeGestion getTypeGestion() {
        return typeGestion;
    }

    public void setTypeGestion(TypeGestion typeGestion) {
        this.typeGestion = typeGestion;
    }
}
