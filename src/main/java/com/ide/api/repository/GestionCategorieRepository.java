package com.ide.api.repository;

import com.ide.api.entities.GestionCategorie;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GestionCategorieRepository extends JpaRepository<GestionCategorie, Integer> {
}
