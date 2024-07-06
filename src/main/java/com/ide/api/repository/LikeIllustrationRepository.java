package com.ide.api.repository;

import com.ide.api.entities.LikeIllustration;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LikeIllustrationRepository extends JpaRepository<LikeIllustration, Integer> {
    boolean existsByDocumentIDAndUtilisateurIP(Integer documentID, String utilisateurIP);
    LikeIllustration findByDocumentIDAndUtilisateurIP(Integer documentID, String utilisateurIP);
    List<LikeIllustration>  findByDocumentID(Integer docID);
}
