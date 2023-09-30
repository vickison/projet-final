package com.ide.api.controller;

import com.ide.api.entities.*;
import com.ide.api.repository.AdminRepository;
import com.ide.api.repository.GestionCategorieRepository;
import com.ide.api.service.AdminService;
import com.ide.api.service.CategorieService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping(path = "categories")
public class CategorieController {

    //Injection de la couche service
    private CategorieService categorieService;
    private AdminService adminService;

    private GestionCategorieRepository gestionCategorieRepository;

    //Le constructeur de notre classe


    public CategorieController(CategorieService categorieService, AdminService adminService, GestionCategorieRepository gestionCategorieRepository) {
        this.categorieService = categorieService;
        this.adminService = adminService;
        this.gestionCategorieRepository = gestionCategorieRepository;
    }

    //La methode qui permet d'ajouter de categorie dans la base, utilisée coté client
    @ResponseStatus(value = HttpStatus.CREATED)
    @PostMapping(value="/ajouter", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void createCategorie(@RequestBody Categorie cat, @RequestParam int idAdmin){
        Admin admin = new Admin();
        admin = adminService.findAdmin(idAdmin);
        GestionCategorie gc = new GestionCategorie();
        gc.setCategorie(cat);
        gc.setAdmin(admin);
        gc.setDateCat(LocalDateTime.now());
        gestionCategorieRepository.save(gc);
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
