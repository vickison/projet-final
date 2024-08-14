package com.ide.api.controller;

import com.ide.api.dto.CategorieDTO;
import com.ide.api.entities.*;
import com.ide.api.enums.TypeGestion;
import com.ide.api.message.ResponseMessage;
import com.ide.api.repository.CategorieDocumentRepository;
import com.ide.api.repository.CategorieRepository;
import com.ide.api.repository.UtilisateurCategorieRepository;
import com.ide.api.repository.UtilisateurRepository;
import com.ide.api.service.*;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import javax.persistence.PostUpdate;
import javax.validation.Valid;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping(path = "categories")
public class CategorieController {

    //Injection de la couche service
    private CategorieRepository categorieRepository;
    private CategorieService categorieService;
    private UtilisateurService utilisateurService;
    private UtilisateurRepository utilisateurRepository;

    private UtilisateurCategorieService utilisateurCategorieService;
    private DocumentService documentService;
    private CacheManager cacheManager;

    public CategorieController(CategorieService categorieService,
                               UtilisateurService utilisateurService,
                               UtilisateurCategorieService utilisateurCategorieService,
                               UtilisateurRepository utilisateurRepository,
                               DocumentService documentService,
                               CategorieRepository categorieRepository,
                               CacheManager cacheManager) {
        this.categorieService = categorieService;
        this.utilisateurService = utilisateurService;
        this.utilisateurCategorieService = utilisateurCategorieService;
        this.utilisateurRepository = utilisateurRepository;
        this.documentService = documentService;
        this.categorieRepository = categorieRepository;
        this.cacheManager = cacheManager;
    }

    @ResponseStatus(value = HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(value="/admin/ajouter", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseMessage> createCategorie(@RequestBody CategorieDTO categorieDTO){
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Integer utilisateurID = userDetails.getId();
        Categorie categorie = new Categorie();
        categorie.setNom(categorieDTO.getNom());
        String message = "";
        try {
            Utilisateur utilisateur = utilisateurService.findUtilisateur(utilisateurID);
            categorie.setAuteurCreationCategorie(utilisateur.getUsername());
            if(utilisateur.isAdmin()){
                categorieService.createCategorie(categorie, utilisateurID);
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

    @GetMapping("/public/{categoryID}/documents")
    public ResponseEntity<List<Document>> findDocumentsByCategoryId(@PathVariable Integer categoryID){
        Categorie categorie = categorieService.findCategory(categoryID);
        List<Document> documents = this.documentService.findDocumentsByCategoryId(categorie);
        return ResponseEntity.ok(documents);
    }

    @GetMapping(value= "/public", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody List<Categorie> findAllCategories(){
        return this.categorieService.findAllCategories();
    }

    @GetMapping(value = "/public/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody Categorie findCategory(@PathVariable Integer id){
       return this.categorieService.findCategory(id);
    }
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/admin/update/{categorieID}")
    //@PutMapping("/update/{id}")
    public ResponseEntity<ResponseMessage> updateCategorie(@PathVariable Integer categorieID,
                                                     @Valid @RequestBody Categorie categorieDetails){
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Integer adminID = userDetails.getId();
        String message = "";
        try{
            this.categorieService.updateCategorie(categorieID, adminID, categorieDetails);
            message = "Categorie mise à jour avec succès...";
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(new ResponseMessage(message));
        }catch (Exception e){
            message = "Echec de mise à jour de categorie...";
            return ResponseEntity
                    .status(HttpStatus.EXPECTATION_FAILED)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(new ResponseMessage(message));
        }
    }
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/admin/delete/{categorieID}")
    //@PutMapping("/update/{id}")
    public ResponseEntity<ResponseMessage> deleteteCategorie(@PathVariable Integer categorieID){
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Integer adminID = userDetails.getId();
        String message = "";
        try{
            this.categorieService.deleteCategorie(categorieID, adminID);
            message = "Categorie supprimée avec succès...";
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(new ResponseMessage(message));
        }catch (Exception e){
            message = "Echec de suppression de categorie...";
            return ResponseEntity
                    .status(HttpStatus.EXPECTATION_FAILED)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(new ResponseMessage(message));
        }
    }
}
