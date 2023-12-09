package com.ide.api.entities;

import com.fasterxml.jackson.annotation.*;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "utilisateur")
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "utilisateurID")
public class Utilisateur implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer utilisateurID;

    private String nom;
    private String prenom;
    private String username;
    @Column(name = "email", unique = true)
    private String email;

    private String password;
    @Column(name = "is_admin", columnDefinition = "boolean default '0'")
    private boolean admin;

    @OneToMany(mappedBy = "utilisateurID", cascade = CascadeType.ALL)
    private Set<UtilisateurAuteur> utilisateurAuteurs = new HashSet<>();

    @OneToMany(mappedBy = "utilisateurID", cascade = CascadeType.ALL)
    private Set<UtilisateurCategorie> utilisateurCategories = new HashSet<>();

    @OneToMany(mappedBy = "utilisateurID", cascade = CascadeType.ALL)
    private Set<UtilisateurDocument> utilisateurDocuments = new HashSet<>();

    @OneToMany(mappedBy = "utilisateurID", cascade = CascadeType.ALL)
    private Set<UtilisateurTag> utilisateurTags = new HashSet<>();

    public Integer getUtilisateurID() {
        return utilisateurID;
    }

    public void setUtilisateurID(Integer utilisateurID) {
        this.utilisateurID = utilisateurID;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public Set<UtilisateurAuteur> getUtilisateurAuteurs() {
        return utilisateurAuteurs;
    }

    public void setUtilisateurAuteurs(Set<UtilisateurAuteur> utilisateurAuteurs) {
        this.utilisateurAuteurs = utilisateurAuteurs;
    }

    public Set<UtilisateurCategorie> getUtilisateurCategories() {
        return utilisateurCategories;
    }

    public void setUtilisateurCategories(Set<UtilisateurCategorie> utilisateurCategories) {
        this.utilisateurCategories = utilisateurCategories;
    }

    public Set<UtilisateurDocument> getUtilisateurDocuments() {
        return utilisateurDocuments;
    }

    public void setUtilisateurDocuments(Set<UtilisateurDocument> utilisateurDocuments) {
        this.utilisateurDocuments = utilisateurDocuments;
    }

    public Set<UtilisateurTag> getUtilisateurTags() {
        return utilisateurTags;
    }

    public void setUtilisateurTags(Set<UtilisateurTag> utilisateurTags) {
        this.utilisateurTags = utilisateurTags;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<GrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority(isAdmin() ? "ROLE_ADMIN" : "ROLE_USER"));
        return authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // Implement as needed
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // Implement as needed
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // Implement as needed
    }

    @Override
    public boolean isEnabled() {
        return true; // Implement as needed
    }
}
