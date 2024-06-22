package com.ide.api.repository;

import com.ide.api.entities.AdminUtilisateur;
import com.ide.api.entities.AdminUtilisateurID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminUtilisateurRepository extends JpaRepository<AdminUtilisateur, AdminUtilisateurID> {
}
