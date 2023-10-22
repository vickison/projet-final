package com.ide.api.repository;

import com.ide.api.entities.UtilisateurAuteur;
import com.ide.api.entities.UtilisateurAuteurID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UtilisateurAuteurRepository extends JpaRepository<UtilisateurAuteur, UtilisateurAuteurID> {
}
