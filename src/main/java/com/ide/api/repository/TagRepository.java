package com.ide.api.repository;

import com.ide.api.entities.Document;
import com.ide.api.entities.Tag;
import com.ide.api.entities.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TagRepository extends JpaRepository<Tag, Integer> {
    List<Tag> findByDocumentTagsDocumentID(Document document);
    List<Tag> findByUtilisateurTagsUtilisateurID(Utilisateur utilisateur);

}
