package com.ide.api.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "tag")
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer tagID;
    private String tag;

    @OneToMany(mappedBy = "tagID")
    private Set<DocumentTag> documentTags = new HashSet<>();

    @OneToMany(mappedBy = "tagID")
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
