package com.ide.api.service;

import com.ide.api.entities.Categorie;
import com.ide.api.repository.CategorieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class CategorieService {

    //Injection de notre repository
    private CategorieRepository categorieRepository;

    //Le contructeur de notre classe
    public CategorieService(CategorieRepository categorieRepository) {
        this.categorieRepository = categorieRepository;
    }

    //La methode qui permet d'ajouter une catégorie dans la base qui prend en param l'objet categorie
    public void createCategorie(Categorie cat) {
         this.categorieRepository.save(cat);
    }


    //La methode qui permet de récupérer la liste des categories
    public List<Categorie> findAllCategories() {
        return categorieRepository.findAll();
    }
}
