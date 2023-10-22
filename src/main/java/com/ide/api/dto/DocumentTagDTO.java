package com.ide.api.dto;

import java.util.Date;

public class DocumentTagDTO {
    private Long tagId;
    private Long documentId;

    public DocumentTagDTO() {
    }

    public DocumentTagDTO(Long tagId, Long documentId) {
        this.tagId = tagId;
        this.documentId = documentId;
    }

    public Long getTagId() {
        return tagId;
    }

    public void setTagId(Long tagId) {
        this.tagId = tagId;
    }

    public Long getDocumentId() {
        return documentId;
    }

    public void setDocumentId(Long documentId) {
        this.documentId = documentId;
    }
}
