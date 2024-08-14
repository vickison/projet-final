package com.ide.api.repository;

import com.ide.api.entities.*;
import com.ide.api.enums.Langue;
import com.ide.api.enums.TypeFichier;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface DocumentRepository extends JpaRepository<Document, Integer>, JpaSpecificationExecutor<Document> {
    List<Document> findByCategorieDocumentsCategorieID(Categorie categorie);
    List<Document> findByUtilisateurDocumentsUtilisateurID(Utilisateur utilisateur);
    List<Document> findByAuteurDocumentsAuteurID(Auteur auteur);
    List<Document> findByDocumentTagsDocumentID(Tag tag);
    @Procedure(name = "Rechercher")
    List<Document> rechercher(String mots);
    @Query(value="SELECT DISTINCT d.* FROM tableDocuments d "+
            "LEFT JOIN tableDocumentEtiquette de ON d.idDocument= de.IdentifiantDocument "+
            "LEFT JOIN tableEtiquettes e ON de.IdentifiantEtiquette = e.idETIQUETTE "+
            "WHERE LOWER(d.TITRE) LIKE LOWER(concat('%', :keyword, '%')) "+
            "OR LOWER(d.DESCRIPTION) LIKE LOWER(concat('%', :keyword, '%')) "+
            "OR LEVENSHTEIN(LOWER(e.ETIQUETTE), LOWER(:keyword)) < 2",
            nativeQuery = true
    )
    List<Document> searchDocumentByKeyWords(@Param("keyword") String keyword);

    List<Document> findAll(Specification<Document> spec);

    List<Document> findByTypeFichier(TypeFichier typeFichier);
    @Query(value = "SELECT d.* FROM tableDocuments d "+
            "ORDER BY d.DateCreationDocument DESC",
            nativeQuery = true)
    List<Document> findAllByOrderDateCreationDocumentDesc();
    @Query(value = "SELECT d.* FROM tableDocuments d "+
            "WHERE d.LANGUE = :langue",
            nativeQuery = true)
    List<Document> findAllByOrderLangue(String langue);
    @Query(value = "SELECT d.* FROM tableDocuments d "+
            "ORDER BY d.TITRE ASC",
            nativeQuery = true)
    List<Document> findAllByOrderTitre();
    //@EntityGraph(attributePaths = {"categorieDocuments", "documentTags", "auteurDocuments", "utilisateurDocuments"})
    Optional<Document> findByDocumentID(Integer documentId);

}
