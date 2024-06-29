package com.ide.api.repository;

import com.ide.api.entities.AdminUtilisateur;
import com.ide.api.entities.AdminUtilisateurID;
import com.ide.api.entities.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdminUtilisateurRepository extends JpaRepository<AdminUtilisateur, AdminUtilisateurID> {
    Optional<AdminUtilisateur> findByadminIDAndUtilisateurID(Utilisateur admin, Utilisateur utilisateur);
}
