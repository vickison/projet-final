package com.ide.api.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "tableEtiquettes")
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idETIQUETTE")
    private Integer tagID;
    @Column(name = "ETIQUETTE")
    private String tag;

    private Timestamp DateCreationEtiquette;
    @Column(name = "AuteurCreationEtiquette")
    private String AdminCreationEtiquette;
    @UpdateTimestamp
    private Timestamp DateModificationEtiquette;
    @Column(name = "AuteurModificationEtiquette")
    private String AdminModificationEtiquette;
    private boolean SupprimerEtiquette;

    @OneToMany(mappedBy = "tagID")
    @JsonIgnore
    private Set<DocumentTag> documentTags = new HashSet<>();

    @OneToMany(mappedBy = "tagID")
    @JsonIgnore
    private Set<UtilisateurTag> utilisateurTags = new HashSet<>();

    public Integer getTagID() {
        return tagID;
    }

    public void setTagID(Integer tagID) {
        this.tagID = tagID;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }



    public String getAdminCreationEtiquette() {
        return AdminCreationEtiquette;
    }

    public void setAdminCreationEtiquette(String adminCreationEtiquette) {
        AdminCreationEtiquette = adminCreationEtiquette;
    }

    public String getAdminModificationEtiquette() {
        return AdminModificationEtiquette;
    }

    public void setAdminModificationEtiquette(String adminModificationEtiquette) {
        AdminModificationEtiquette = adminModificationEtiquette;
    }

    public Timestamp getDateCreationEtiquette() {
        return DateCreationEtiquette;
    }

    public void setDateCreationEtiquette(Timestamp dateCreationEtiquette) {
        DateCreationEtiquette = dateCreationEtiquette;
    }

    public Timestamp getDateModificationEtiquette() {
        return DateModificationEtiquette;
    }

    public void setDateModificationEtiquette(Timestamp dateModificationEtiquette) {
        DateModificationEtiquette = dateModificationEtiquette;
    }

    public boolean getSupprimerEtiquette() {
        return SupprimerEtiquette;
    }

    public void setSupprimerEtiquette(boolean supprimerEtiquette) {
        SupprimerEtiquette = supprimerEtiquette;
    }

    public Set<DocumentTag> getDocumentTags() {
        return documentTags;
    }

    public void setDocumentTags(Set<DocumentTag> documentTags) {
        this.documentTags = documentTags;
    }

    public Set<UtilisateurTag> getUtilisateurTags() {
        return utilisateurTags;
    }

    public void setUtilisateurTags(Set<UtilisateurTag> utilisateurTags) {
        this.utilisateurTags = utilisateurTags;
    }
}
