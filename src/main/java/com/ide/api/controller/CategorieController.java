package com.ide.api.controller;

import com.ide.api.dto.CategorieDTO;
import com.ide.api.entities.*;
import com.ide.api.message.ResponseMessage;
import com.ide.api.repository.CategorieDocumentRepository;
import com.ide.api.repository.UtilisateurRepository;
import com.ide.api.service.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping(path = "categories")
public class CategorieController {

    //Injection de la couche service
    private CategorieService categorieService;
    private UtilisateurService utilisateurService;
    private UtilisateurRepository utilisateurRepository;

    private UtilisateurCategorieService utilisateurCategorieService;
    private DocumentService documentService;

    public CategorieController(CategorieService categorieService,
                               UtilisateurService utilisateurService,
                               UtilisateurCategorieService utilisateurCategorieService,
                               UtilisateurRepository utilisateurRepository,
                               DocumentService documentService) {
        this.categorieService = categorieService;
        this.utilisateurService = utilisateurService;
        this.utilisateurCategorieService = utilisateurCategorieService;
        this.utilisateurRepository = utilisateurRepository;
        this.documentService = documentService;
    }

    @ResponseStatus(value = HttpStatus.CREATED)
    @PostMapping(value="/ajouter", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseMessage> createCategorie(@RequestBody Categorie categorie,
                                                           @RequestParam int utilisateurID){
        String message = "";
        try {
            Utilisateur utilisateur = utilisateurService.findUtilisateur(utilisateurID);
            if(utilisateur.isAdmin()){
                categorieService.createCategorie(categorie);
                UtilisateurCategorie utilisateurCategorie = new UtilisateurCategorie();
                utilisateurCategorie.setCategorie(categorie);
                utilisateurCategorie.setUtilisateur(utilisateur);
                utilisateurCategorie.setDateCreation(new Date());
                utilisateurCategorieService.createUtilisateurCategorie(utilisateurCategorie);
                message = "Categorie créée avec succès...";
                return ResponseEntity
                        .status(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(new ResponseMessage(message));
            }else {
                message = "Cet utilisateur n'a pas ce privillège...";
                return ResponseEntity
                        .status(HttpStatus.EXPECTATION_FAILED)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(new ResponseMessage(message));
            }

        }catch (Exception e){
            message = "Echec de création de categorie...";
            return ResponseEntity
                    .status(HttpStatus.EXPECTATION_FAILED)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(new ResponseMessage(message));
        }
    }

    @GetMapping("/{categoryID}/documents")
    public ResponseEntity<List<Document>> findDocumentsByCategoryId(@PathVariable Integer categoryID){
        Categorie categorie = categorieService.findCategory(categoryID);
        List<Document> documents = this.documentService.findDocumentsByCategoryId(categorie);
        return ResponseEntity.ok(documents);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody List<Categorie> findAllCategories(){
        return this.categorieService.findAllCategories();
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody Categorie findCategory(@PathVariable Integer id){
       return this.categorieService.findCategory(id);
    }
}
