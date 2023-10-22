package com.ide.api.repository;

import com.ide.api.entities.UtilisateurCategorie;
import com.ide.api.entities.UtilisateurCategorieID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UtilisateurCategorieRepository extends JpaRepository<UtilisateurCategorie, UtilisateurCategorieID> {
}
