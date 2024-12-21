package com.ide.api.repository;

import com.ide.api.dto.CategorieDocumentDTO;
import com.ide.api.entities.CategorieDocument;
import com.ide.api.entities.CategorieDocumentID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CategorieDocumentRepository extends JpaRepository<CategorieDocument, CategorieDocumentID> {
    @Query(
            "select new com.ide.api.dto.CategorieDocumentDTO(c.categorieID, d.documentID, "+
                    "c.nom, d.titre, d.url, d.format, d.resume, d.thumbnail) "+
                    "from Categorie c "+
                    "LEFT JOIN CategorieDocument dc ON c.categorieID = dc.categorieID.categorieID " +
                    "LEFT JOIN Document d ON dc.documentID.documentID = d.documentID "+
                    "order by c.categorieID"
    )
    List<CategorieDocumentDTO> findCategoriesAndDocuments();
}
