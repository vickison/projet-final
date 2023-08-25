package com.ide.api.controller;

import com.ide.api.entities.Categorie;
import com.ide.api.entities.Document;
import com.ide.api.service.CategorieService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping(path = "categories")
public class CategorieController {

    //Injection de la couche service
    private CategorieService categorieService;

    //Le constructeur de notre classe
    public CategorieController(CategorieService categorieService) {
        this.categorieService = categorieService;
    }

    //La methode qui permet d'ajouter de categorie dans la base, utilisée coté client
    @ResponseStatus(value = HttpStatus.CREATED)
    @PostMapping(value="/ajouter", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void createCategorie(@RequestBody Categorie cat){
        this.categorieService.createCategorie(cat);
    }


    //La methode qui permet d'extraire toutes categories dans la base, utilisée coté client
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody List<Categorie> findAllCategories(){
        return this.categorieService.findAllCategories();
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody Categorie findCategory(@PathVariable Integer id){
       return this.categorieService.findCategory(id);
    }
}
