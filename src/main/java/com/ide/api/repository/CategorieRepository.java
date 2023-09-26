package com.ide.api.repository;

import com.ide.api.entities.Categorie;
import com.ide.api.entities.Document;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategorieRepository extends JpaRepository<Categorie, Integer> {

}
