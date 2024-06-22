package com.ide.api.entities;

import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@DynamicInsert
@IdClass(DocumentTagID.class)
@Table(name = "tableDocumentEtiquette")
public class DocumentTag implements Serializable {

    @Id
    @ManyToOne
    @JoinColumn(name = "IdentifiantDocument")
    private Document documentID;

    @Id
    @ManyToOne
    @JoinColumn(name = "IdentifiantEtiquette")
    private Tag tagID;

    public Document getDocument() {
        return documentID;
    }

    public void setDocument(Document document) {
        this.documentID = document;
    }

    public Tag getTag() {
        return tagID;
    }

    public void setTag(Tag tag) {
        this.tagID = tag;
    }
}
