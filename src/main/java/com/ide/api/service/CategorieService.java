package com.ide.api.service;

import com.ide.api.dto.CategorieDTO;
import com.ide.api.entities.*;
import com.ide.api.repository.CategorieDocumentRepository;
import com.ide.api.repository.CategorieRepository;
import com.ide.api.repository.DocumentRepository;
import com.ide.api.repository.UtilisateurRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;


@Service
public class CategorieService {

    private CategorieRepository categorieRepository;
    private CategorieDocumentRepository categorieDocumentRepository;
    private DocumentRepository documentRepository;
    private UtilisateurRepository utilisateurRepository;

    public CategorieService(CategorieRepository categorieRepository,
                            CategorieDocumentRepository categorieDocumentRepository,
                            DocumentRepository documentRepository,
                            UtilisateurRepository utilisateurRepository) {
        this.categorieRepository = categorieRepository;
        this.categorieDocumentRepository = categorieDocumentRepository;
        this.documentRepository = documentRepository;
        this.utilisateurRepository = utilisateurRepository;
    }

    /*private CategorieDTO convertToDTO(Categorie categorie) {
        CategorieDTO categorieDTO = new CategorieDTO();
        categorieDTO.setCategorieID(categorie.getCategorieID());
        categorieDTO.setNom(categorie.getNom());
        List<Document> documents = documentRepository.findAll();
        List<String> documentNames = new ArrayList<>();
        List<Integer> documentIds = new ArrayList<>();
        for (Document document: documents){
            do;
        }
        categorieDTO.setDocuments(documents);
        for(Document document: documents)

        List<Utilisateur> utilisateurs = utilisateurRepository.findAll();
        categorieDTO.setUtlisateurs(utilisateurs);
        return categorieDTO;
    }*/

    public void createCategorie(Categorie categorie) {
        this.categorieRepository.save(categorie);
    }

    public List<Categorie> findAllCategories() {
        return this.categorieRepository.findAll();
    }

    public Categorie findCategory(Integer id){
        return this.categorieRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Categorie not found with id: " + id));
    }
}
