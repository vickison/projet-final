package com.ide.api.service;

import com.ide.api.dto.CategorieDTO;
import com.ide.api.entities.*;
import com.ide.api.enums.TypeGestion;
import com.ide.api.repository.*;
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
    private UtilisateurCategorieRepository utilisateurCategorieRepository;

    public CategorieService(CategorieRepository categorieRepository,
                            CategorieDocumentRepository categorieDocumentRepository,
                            DocumentRepository documentRepository,
                            UtilisateurRepository utilisateurRepository,
                            UtilisateurCategorieRepository utilisateurCategorieRepository) {
        this.categorieRepository = categorieRepository;
        this.categorieDocumentRepository = categorieDocumentRepository;
        this.documentRepository = documentRepository;
        this.utilisateurRepository = utilisateurRepository;
        this.utilisateurCategorieRepository = utilisateurCategorieRepository;
    }

    public void createCategorie(Categorie categorie,
                                Integer idUtilisateur) {
        Categorie categorieSaved = this.categorieRepository.save(categorie);
        UtilisateurCategorie utilisateurCategorie = new UtilisateurCategorie();
        Utilisateur utilisateur = this.utilisateurRepository.findById(idUtilisateur)
                .orElseThrow(() -> new EntityNotFoundException("Utilisateur avec identifiant: " + idUtilisateur + " introuvable"));
        utilisateurCategorie.setCategorieID(categorieSaved);
        utilisateurCategorie.setUtilisateurID(utilisateur);
        utilisateurCategorie.setTypeGestion(TypeGestion.Ajouter);
        this.utilisateurCategorieRepository.save(utilisateurCategorie);
    }

    public List<Categorie> findAllCategories() {
        return this.categorieRepository.findAll();
    }

    public Categorie findCategory(Integer id){
        return this.categorieRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Utilisateur avec identifiant: " + id + " introuvable"));
    }

    public List<Categorie> findCategoriesByUtilisateurId(Utilisateur utilisateur){
        return this.categorieRepository.findByUtilisateurCategoriesUtilisateurID(utilisateur);
    }

    public void updateCategorie(Integer documentID,
                               Integer adminID){
        Categorie existingCategorie = this.categorieRepository.findById(documentID)
                .orElseThrow(() -> new EntityNotFoundException("Categorie avec identifiant: " + documentID + " introuvable"));

    }
}
