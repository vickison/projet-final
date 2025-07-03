package com.ide.api.controller;

import com.ide.api.dto.CategorieDTO;
import com.ide.api.entities.Categorie;
import com.ide.api.entities.CustomUserDetails;
import com.ide.api.entities.Document;
import com.ide.api.entities.Utilisateur;
import com.ide.api.message.ResponseMessage;
import com.ide.api.service.CategorieService;
import com.ide.api.service.DocumentService;
import com.ide.api.service.UtilisateurService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping(path = "categories")
public class CategorieController {
    private static final Logger logger = LoggerFactory.getLogger(CategorieController.class);

    private CategorieService categorieService;
    private UtilisateurService utilisateurService;

    private DocumentService documentService;


    public CategorieController(CategorieService categorieService,
                               UtilisateurService utilisateurService,
                               DocumentService documentService
                               ) {
        this.categorieService = categorieService;
        this.utilisateurService = utilisateurService;
        this.documentService = documentService;
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
    //@Timed(value = "categories.findDocumentsByCat", description = "Temps pour rechercher des documents d'une categorie")
    public ResponseEntity<List<Document>> findDocumentsByCategoryId(@PathVariable Integer categoryID){
        Categorie categorie = categorieService.findCategory(categoryID);
        List<Document> documents = this.documentService.findDocumentsByCategoryId(categorie);
        return ResponseEntity.ok(documents);
    }


//    @GetMapping("/public/{categoryID}/documents")
//    public ResponseEntity<Page<Document>> findDocumentsByCategoryIdWithPagination(
//            @PathVariable Integer categoryID,
//            @RequestParam(defaultValue = "0") int page,
//            @RequestParam(defaultValue = "10") int size,
//            @RequestParam(required = false) String sort) {
//
//        // Validation de l'ID de catégorie
//        Categorie categorie = categorieService.findCategory(categoryID);
//        if (categorie == null) {
//            return ResponseEntity.notFound().build();
//        }
//
//        // Création du Pageable avec tri optionnel
//        Pageable pageable = (sort != null)
//                ? PageRequest.of(page, size, Sort.by(sort))
//                : PageRequest.of(page, size);
//
//        Page<Document> documentsPage = documentService.findDocumentsByCategoryIdWithPagination(categorie, pageable);
//        return ResponseEntity.ok(documentsPage);
//    }

    @GetMapping(value= "/public", produces = MediaType.APPLICATION_JSON_VALUE)
    public  List<Categorie> findAllCategories(){
        return this.categorieService.findAllCategories();
    }

    @GetMapping(value = "/public/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public  Categorie findCategory(@PathVariable Integer id){
       return this.categorieService.findCategory(id);
    }
    @PutMapping("/admin/update/{categorieID}")
    @PreAuthorize("hasRole('ADMIN')")
    //@CachePut(value = "categorieCache", key = "#categorieID")
    public Categorie updateCategorie(@PathVariable Integer categorieID,
                                                     @Valid @RequestBody CategorieDTO categorieDetails){
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Integer adminID = userDetails.getId();
        try{
            Categorie finalCategory = this.categorieService.updateCategorie(categorieID, adminID, categorieDetails);
            logger.info("Mise à jour de la catégorie ID {}", categorieID);
            return finalCategory;
        }catch (Exception e){
            throw new RuntimeException("Erreur lors de la mise à jour de la catégorie avec ID: " + categorieID, e);
        }
    }
    @PutMapping("/admin/delete/{categorieID}")
    @PreAuthorize("hasRole('ADMIN')")
    //@CachePut(value = "categorieCache", key = "#categorieID")
    public Categorie deleteteCategorie(@PathVariable Integer categorieID){
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Integer adminID = userDetails.getId();
        try{
            Categorie categorie = this.categorieService.deleteCategorie(categorieID, adminID);
            return categorie;
        }catch (Exception e){
            throw new RuntimeException("Erreur lors de la suppression de la catégorie avec ID: " + categorieID, e);
        }
    }

    @GetMapping("/public/categorie/documents")
    public List<Categorie> getCategoriesWithDocuments() {
        return categorieService.getCategoriesWithDocuments();
    }
}
