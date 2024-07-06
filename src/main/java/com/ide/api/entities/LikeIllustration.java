package com.ide.api.entities;

import com.ide.api.enums.Mention;

import javax.persistence.*;

@Entity
@Table(name = "like_illustration")
public class LikeIllustration {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "like_id")
    private Integer likeID;
    @Column(name = "document_id")
    private Integer documentID;
    @Column(name = "utilisateur_ip")
    private String utilisateurIP;
    @Enumerated(EnumType.STRING)
    private Mention mention;

    public Integer getLikeID() {
        return likeID;
    }

    public void setLikeID(Integer likeID) {
        this.likeID = likeID;
    }

    public Integer getDocumentID() {
        return documentID;
    }

    public void setDocumentID(Integer documentID) {
        this.documentID = documentID;
    }

    public String getUtilisateurIP() {
        return utilisateurIP;
    }

    public void setUtilisateurIP(String utilisateurIP) {
        this.utilisateurIP = utilisateurIP;
    }

    public Mention getMention() {
        return mention;
    }

    public void setMention(Mention mention) {
        this.mention = mention;
    }
}
