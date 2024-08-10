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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;


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

    @Transactional
    public void createCategorie(Categorie categorie, Integer idUtilisateur) {
        Logger logger = LoggerFactory.getLogger(getClass());

        try {
            if (categorie == null || idUtilisateur == null) {
                throw new IllegalArgumentException("La catégorie ou l'identifiant de l'utilisateur ne peut pas être null");
            }
            Categorie categorieSaved = this.categorieRepository.save(categorie);
            Utilisateur utilisateur = this.utilisateurRepository.findById(idUtilisateur)
                    .orElseThrow(() -> new EntityNotFoundException("Utilisateur avec identifiant : " + idUtilisateur + " introuvable"));


            UtilisateurCategorie utilisateurCategorie = new UtilisateurCategorie();
            utilisateurCategorie.setCategorieID(categorieSaved);
            utilisateurCategorie.setUtilisateurID(utilisateur);
            utilisateurCategorie.setTypeGestion(TypeGestion.Ajouter);

            this.utilisateurCategorieRepository.save(utilisateurCategorie);

        } catch (Exception e) {
            logger.error("Erreur lors de la création de la catégorie ou de la relation utilisateur-catégorie", e);
            throw new RuntimeException("Erreur lors de la création de la catégorie ou de la relation utilisateur-catégorie", e);
        }
    }

    public List<Categorie> findAllCategories() {
        Logger logger = LoggerFactory.getLogger(getClass());

        try {

            List<Categorie> categories = this.categorieRepository.findAll();
            logger.info("Nombre de catégories récupérées : {}", categories.size());

            return categories;

        } catch (Exception e) {
            logger.error("Erreur lors de la récupération des catégories", e);
            throw new RuntimeException("Erreur lors de la récupération des catégories", e);
        }
    }

    public Categorie findCategory(Integer id) {
        try {
            // Rechercher la catégorie par ID
            return this.categorieRepository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("Catégorie avec identifiant: " + id + " introuvable"));
        } catch (Exception e) {
            // Logger l'erreur et lancer une exception Runtime
            Logger logger = LoggerFactory.getLogger(getClass());
            logger.error("Erreur lors de la recherche de la catégorie avec ID: {}", id, e);
            throw new RuntimeException("Erreur lors de la recherche de la catégorie avec ID: " + id, e);
        }
    }

    public List<Categorie> findCategoriesByUtilisateurId(Utilisateur utilisateur) {
        try {
            // Vérifier si l'utilisateur existe
            if (utilisateur == null || utilisateur.getUtilisateurID() == null) {
                throw new IllegalArgumentException("Utilisateur est nul ou l'identifiant est nul.");
            }

            List<Categorie> categories = this.categorieRepository.findByUtilisateurCategoriesUtilisateurID(utilisateur);
            if (categories.isEmpty()) {
                throw new EntityNotFoundException("Aucune catégorie trouvée pour l'utilisateur avec identifiant: " + utilisateur.getUtilisateurID());
            }

            return categories;
        } catch (Exception e) {
            Logger logger = LoggerFactory.getLogger(getClass());
            logger.error("Erreur lors de la recherche des catégories pour l'utilisateur avec ID: {}", utilisateur != null ? utilisateur.getUtilisateurID() : "inconnu", e);
            throw new RuntimeException("Erreur lors de la recherche des catégories pour l'utilisateur avec ID: " + (utilisateur != null ? utilisateur.getUtilisateurID() : "inconnu"), e);
        }
    }

    @Transactional
    public void updateCategorie(Integer categorieID, Integer adminID, Categorie updatedCategorieData) {
        try {
            // Vérifier si l'identifiant de la catégorie est valide
            if (categorieID == null || adminID == null) {
                throw new IllegalArgumentException("L'identifiant de la catégorie ou de l'administrateur est nul.");
            }

            // Trouver la catégorie existante
            Categorie existingCategorie = this.categorieRepository.findById(categorieID)
                    .orElseThrow(() -> new EntityNotFoundException("Catégorie avec identifiant: " + categorieID + " introuvable"));

            // Mettre à jour les champs de la catégorie avec les nouvelles données
            existingCategorie.setNom(updatedCategorieData.getNom());
            // Ajoutez ici d'autres champs à mettre à jour si nécessaire

            // Vérifier si l'administrateur existe (si nécessaire)
            Utilisateur admin = this.utilisateurRepository.findById(adminID)
                    .orElseThrow(() -> new EntityNotFoundException("Administrateur avec identifiant: " + adminID + " introuvable"));

            // Optionnel: Enregistrer une entrée dans l'historique des modifications ou des logs
            // HistoriqueModification modification = new HistoriqueModification();
            // modification.setCategorie(existingCategorie);
            // modification.setAdmin(admin);
            // modification.setAction(TypeGestion.Modifier);
            // this.historiqueModificationRepository.save(modification);

            // Sauvegarder les modifications dans la base de données
            this.categorieRepository.save(existingCategorie);

        } catch (Exception e) {
            // Logger l'erreur et lancer une exception Runtime
            Logger logger = LoggerFactory.getLogger(getClass());
            logger.error("Erreur lors de la mise à jour de la catégorie avec ID: {}", categorieID, e);
            throw new RuntimeException("Erreur lors de la mise à jour de la catégorie avec ID: " + categorieID, e);
        }
    }

}
