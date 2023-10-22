package com.ide.api.service;

import com.ide.api.entities.UtilisateurCategorie;
import com.ide.api.repository.UtilisateurCategorieRepository;
import org.springframework.stereotype.Service;

@Service
public class UtilisateurCategorieService {
    private UtilisateurCategorieRepository utilisateurCategorieRepository;

    public UtilisateurCategorieService(UtilisateurCategorieRepository utilisateurCategorieRepository) {
        this.utilisateurCategorieRepository = utilisateurCategorieRepository;
    }

    public void createUtilisateurCategorie(UtilisateurCategorie utilisateurCategorie){
        this.utilisateurCategorieRepository.save(utilisateurCategorie);
    }
}
