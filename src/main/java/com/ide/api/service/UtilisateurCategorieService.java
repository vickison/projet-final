package com.ide.api.service;

import com.ide.api.entities.Categorie;
import com.ide.api.entities.Utilisateur;
import com.ide.api.entities.UtilisateurCategorie;
import com.ide.api.enums.TypeGestion;
import com.ide.api.repository.UtilisateurCategorieRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class UtilisateurCategorieService {
    private UtilisateurCategorieRepository utilisateurCategorieRepository;

    public UtilisateurCategorieService(UtilisateurCategorieRepository utilisateurCategorieRepository) {
        this.utilisateurCategorieRepository = utilisateurCategorieRepository;
    }

    public void createUtilisateurCategorie(UtilisateurCategorie utilisateurCategorie){
        this.utilisateurCategorieRepository.save(utilisateurCategorie);
    }


//    public UtilisateurCategorie findUtilCatByUtilID(Utilisateur utilisateur){
//        return this.utilisateurCategorieRepository.findByUtilisateurID(utilisateur);
//    }
//
    public UtilisateurCategorie findUtilCatByCatID(Categorie categorieID){
        List<UtilisateurCategorie> utilisateurCategories = utilisateurCategorieRepository.findByCategorieID(categorieID);
        UtilisateurCategorie utilisateurCategorie = new UtilisateurCategorie();
        for(UtilisateurCategorie uc : utilisateurCategories){
            if(uc.getTypeGestion() == TypeGestion.Ajouter){
                utilisateurCategorie = uc;
            }
        }
        return utilisateurCategorie;
    }
//    public UtilisateurCategorie createOrUpdateUtilisateurCategorie(Categorie categorieUp,
//                                                                   Integer utilisateurID){
//        UtilisateurCategorie existingUtilisateurCategorie = this.utilisateurCategorieRepository.findByUtilisateurIDAndCategorieID(utilisateurCategorie.getUtilisateurID(), utilisateurCategorie.getCategorieID());
//
//        if(existingUtilisateurCategorie != null){
//            existingUtilisateurCategorie.setTypeGestion(TypeGestion.Modifier);
//            return this.utilisateurCategorieRepository.save(existingUtilisateurCategorie);
//        }else {
//            utilisateurCategorie.setUtilisateurID(utilisateur);
//            utilisateurCategorie.setCategorieID(categorie);
//            utilisateurCategorie.setTypeGestion(TypeGestion.Modifier);
//            return this.utilisateurCategorieRepository.save(utilisateurCategorie);
//        }
//    }
}
