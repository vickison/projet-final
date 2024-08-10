package com.ide.api.message;

public class DocumentCreationResponse {
    private String message;

    public DocumentCreationResponse(String message) {
        this.message = message;
    }

    // Getter et Setter
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
