package com.ide.api.entities;

import com.fasterxml.jackson.annotation.*;
import lombok.Builder;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "tableUtilisateurs")
public class Utilisateur implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idUtil")
    private Integer utilisateurID;
    @Column(name = "NomUtil")
    private String nom;
    @Column(name = "PrenomUtil")
    private String prenom;
    @Column(name = "USERNAME")
    private String username;
    @Column(name = "EmailUtil", unique = true)
    private String email;
    @Column(name = "MotDePasse")
    private String password;
    @Column(name = "ADMINISTRATEUR")
    private boolean admin;
    private String AddresseIP;
    private Timestamp DateCreationUtil;
    private String AuteurCreationUtil;
    @UpdateTimestamp
    private  Timestamp DateModificationUtil;
    private String AuteurModificationUtil;
    private boolean SupprimerUtil;
    private boolean SuperAdmin;



    @OneToMany(mappedBy = "utilisateurID", cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<UtilisateurAuteur> utilisateurAuteurs = new HashSet<>();

    @OneToMany(mappedBy = "utilisateurID", cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<UtilisateurCategorie> utilisateurCategories = new HashSet<>();

    @OneToMany(mappedBy = "utilisateurID", cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<UtilisateurDocument> utilisateurDocuments = new HashSet<>();

    @OneToMany(mappedBy = "utilisateurID", cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<UtilisateurTag> utilisateurTags = new HashSet<>();
    @OneToMany(mappedBy = "utilisateurID", cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<AdminUtilisateur> adminUtilisateurs = new HashSet<>();


    public Utilisateur() {
    }

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

    public String getAddresseIP() {
        return AddresseIP;
    }

    public void setAddresseIP(String addresseIP) {
        AddresseIP = addresseIP;
    }

    public Timestamp getDateCreationUtil() {
        return DateCreationUtil;
    }

    public void setDateCreationUtil(Timestamp dateCreationUtil) {
        DateCreationUtil = dateCreationUtil;
    }

    public String getAuteurCreationUtil() {
        return AuteurCreationUtil;
    }

    public void setAuteurCreationUtil(String auteurCreationUtil) {
        this.AuteurCreationUtil = auteurCreationUtil == null? "root": auteurCreationUtil;
    }

    public Timestamp getDateModificationUtil() {
        return DateModificationUtil;
    }

    public void setDateModificationUtil(Timestamp dateModificationUtil) {
        DateModificationUtil = dateModificationUtil;
    }

    public String getAuteurModificationUtil() {
        return AuteurModificationUtil;
    }

    public void setAuteurModificationUtil(String auteurModificationUtil) {
        AuteurModificationUtil = auteurModificationUtil;
    }

    public boolean isSupprimerUtil() {
        return SupprimerUtil;
    }

    public void setSupprimerUtil(boolean supprimerUtil) {
        SupprimerUtil = supprimerUtil;
    }

    public boolean isSuperAdmin() {
        return SuperAdmin;
    }

    public void setSuperAdmin(boolean superAdmin) {
        SuperAdmin = superAdmin;
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

    public Set<AdminUtilisateur> getAdminUtilisateurs() {
        return adminUtilisateurs;
    }

    public void setAdminUtilisateurs(Set<AdminUtilisateur> adminUtilisateurs) {
        this.adminUtilisateurs = adminUtilisateurs;
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
