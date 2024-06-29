package com.ide.api.repository;

import com.ide.api.entities.Tag;
import com.ide.api.entities.Utilisateur;
import com.ide.api.entities.UtilisateurTag;
import com.ide.api.entities.UtilisateurTagID;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UtilisateurTagRepository extends JpaRepository<UtilisateurTag, UtilisateurTagID> {
    UtilisateurTag findByUtilisateurID(Utilisateur utilisateur);
    List<UtilisateurTag> findByTagID(Tag tag);

    Optional<UtilisateurTag> findByTagIDAndUtilisateurID(Tag tag, Utilisateur utilisateur);
}
