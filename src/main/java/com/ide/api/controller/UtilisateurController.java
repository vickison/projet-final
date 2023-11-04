package com.ide.api.controller;

import com.ide.api.entities.*;
import com.ide.api.message.ResponseMessage;
import com.ide.api.service.*;
import com.ide.api.utilities.EmailValidator;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping(path = "")
public class UtilisateurController {
    private UtilisateurService utilisateurService;
    private DocumentService documentService;
    private TagService tagService;
    private CategorieService categorieService;
    private AuteurService auteurService;

    public UtilisateurController(UtilisateurService utilisateurService,
                                 DocumentService documentService,
                                 TagService tagService,
                                 CategorieService categorieService,
                                 AuteurService auteurService) {
        this.utilisateurService = utilisateurService;
        this.documentService = documentService;
        this.tagService = tagService;
        this.categorieService = categorieService;
        this.auteurService = auteurService;
    }


    @ResponseStatus(value = HttpStatus.CREATED)
    @PostMapping(value="/register", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseMessage> createUser(@RequestBody Utilisateur utilisateur){
        String message = "";
        try{
            if(EmailValidator.isValid(utilisateur.getEmail())) {
                utilisateur.setAdmin(false);
                this.utilisateurService.createUtilisateur(utilisateur);
                message = "Utilisateur créé avec succès...";
                return ResponseEntity
                        .status(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(new ResponseMessage(message));
            }else{
                message = "Email incorrect...";
                return ResponseEntity
                        .status(HttpStatus.EXPECTATION_FAILED)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(new ResponseMessage(message));
            }

        }catch (Exception e){
            message = "Echec de création d'utilisateur...";
            return ResponseEntity
                    .status(HttpStatus.EXPECTATION_FAILED)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(new ResponseMessage(message));
        }
    }


    @ResponseStatus(value = HttpStatus.CREATED)
    @PostMapping(value="/admin/register", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseMessage> createAdmin(@RequestBody Utilisateur utilisateur){
        String message = "";
        try{
            if(EmailValidator.isValid(utilisateur.getEmail())) {
                utilisateur.setAdmin(true);
                this.utilisateurService.createUtilisateur(utilisateur);
                message = "Utilisateur créé avec succès...";
                return ResponseEntity
                        .status(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(new ResponseMessage(message));
            }else{
                message = "Email incorrect...";
                return ResponseEntity
                        .status(HttpStatus.EXPECTATION_FAILED)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(new ResponseMessage(message));
            }

        }catch (Exception e){
            message = "Echec de création d'utilisateur...";
            return ResponseEntity
                    .status(HttpStatus.EXPECTATION_FAILED)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(new ResponseMessage(message));
        }
    }

    @GetMapping(value = "/users", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody List<Utilisateur> findUsers(){
        return this.utilisateurService.findUtilisateurs();
    }

    @GetMapping(value = "/user/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody Utilisateur findUser(@PathVariable Integer id){
        return this.utilisateurService.findUtilisateur(id);
    }

    @GetMapping("/user/{utilisateurID}/documents")
    public ResponseEntity<List<Document>> findDocumentsByUtilisateurId(@PathVariable Integer utilisateurID){
        Utilisateur utilisateur = this.utilisateurService.findUtilisateur(utilisateurID);
        List<Document> documents = this.documentService.findDocumentsByUtilisateurId(utilisateur);
        return ResponseEntity.ok(documents);
    }

    @GetMapping("/user/{utilisateurID}/tags")
    public ResponseEntity<List<Tag>> findTagsByUtilisateurId(@PathVariable Integer utilisateurID){
        Utilisateur utilisateur = this.utilisateurService.findUtilisateur(utilisateurID);
        List<Tag> tags = this.tagService.findTagsByUtilisateurId(utilisateur);
        return ResponseEntity.ok(tags);
    }

    @GetMapping("/user/{utilisateurID}/categories")
    public ResponseEntity<List<Categorie>> findCategoriesByUtilisateurId(@PathVariable Integer utilisateurID){
        Utilisateur utilisateur = this.utilisateurService.findUtilisateur(utilisateurID);
        List<Categorie> categories = this.categorieService.findCategoriesByUtilisateurId(utilisateur);
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/user/{utilisateurID}/auteurs")
    public ResponseEntity<List<Auteur>> findAuteurByUtilisateurId(@PathVariable Integer utilisateurID){
        Utilisateur utilisateur = this.utilisateurService.findUtilisateur(utilisateurID);
        List<Auteur> auteurs = this.auteurService.findAuteursByUtilisateurId(utilisateur);
        return ResponseEntity.ok(auteurs);
    }


}
