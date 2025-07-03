package com.ide.api.dto;

public class PasswordVerificationRequest {
    private Integer userId;       // ID de l'utilisateur
    private String password;   // Mot de passe à vérifier (en clair, non haché)

    // Constructeurs
    public PasswordVerificationRequest() {}

    public PasswordVerificationRequest(Integer userId, String password) {
        this.userId = userId;
        this.password = password;
    }

    // Getters et Setters
    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
