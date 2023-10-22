package com.ide.api.entities;

import javax.persistence.*;

@Entity
@IdClass(DocumentTagID.class)
@Table(name = "document_tag")
public class DocumentTag {

    @Id
    @ManyToOne
    @JoinColumn(name = "documentID")
    private Document documentID;

    @Id
    @ManyToOne
    @JoinColumn(name = "tagID")
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
