package com.ide.api.repository;

import com.ide.api.entities.UtilisateurTag;
import com.ide.api.entities.UtilisateurTagID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UtilisateurTagRepository extends JpaRepository<UtilisateurTag, UtilisateurTagID> {
}
