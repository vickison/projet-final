package com.ide.api.repository;

import com.ide.api.dto.LikeCountDTO;
import com.ide.api.entities.LikeIllustration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LikeIllustrationRepository extends JpaRepository<LikeIllustration, Integer> {
    boolean existsByDocumentIDAndUtilisateurIP(Integer documentID, String utilisateurIP);
    LikeIllustration findByDocumentIDAndUtilisateurIP(Integer documentID, String utilisateurIP);
    List<LikeIllustration>  findByDocumentID(Integer docID);
    @Query(value = "SELECT li.mention, COUNT(*) AS count\n" +
            "FROM like_illustration li\n" +
            "WHERE li.document_id = :documentId\n" +
            "GROUP BY li.mention", nativeQuery = true)
    List<Object[]> countByMention(@Param("documentId") Integer documentId);
    @Query(value = "SELECT COUNT(*)\n" +
            "FROM like_illustration li\n" +
            "WHERE li.document_id = :documentId AND li.mention = 'like'", nativeQuery = true)
    Integer countLikes(@Param("documentId") Integer documentId);

    @Query(value = "SELECT COUNT(*)\n" +
            "FROM like_illustration li\n" +
            "WHERE li.document_id = :documentId AND li.mention = 'unlike'", nativeQuery = true)
    Integer countUnlikes(@Param("documentId") Integer documentId);
}
