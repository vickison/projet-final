package com.ide.api.controller;

import com.ide.api.entities.Auteur;
import com.ide.api.entities.Categorie;
import com.ide.api.entities.Utilisateur;
import com.ide.api.entities.UtilisateurAuteur;
import com.ide.api.message.ResponseMessage;
import com.ide.api.service.AuteurService;
import com.ide.api.service.UtilisateurAuteurService;
import com.ide.api.service.UtilisateurService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping(path = "auteurs")
public class AuteurController {
    private AuteurService auteurService;
    private UtilisateurService utilisateurService;
    private UtilisateurAuteurService utilisateurAuteurService;

    public AuteurController(AuteurService auteurService,
                            UtilisateurService utilisateurService,
                            UtilisateurAuteurService utilisateurAuteurService) {
        this.auteurService = auteurService;
        this.utilisateurService = utilisateurService;
        this.utilisateurAuteurService = utilisateurAuteurService;
    }

    @ResponseStatus(value = HttpStatus.CREATED)
    @PostMapping(value="/ajouter", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseMessage> createAuteur(@RequestBody Auteur au,
                                                        @RequestParam(value = "U") Integer utilisateurID){
        String message = "";
        try{
            Utilisateur utilisateur = this.utilisateurService.findUtilisateur(utilisateurID);
            if(utilisateur.isAdmin()){
                this.auteurService.createAuteur(au);
                UtilisateurAuteur utilisateurAuteur = new UtilisateurAuteur();
                utilisateurAuteur.setAuteur(au);
                utilisateurAuteur.setUtilisateur(utilisateur);
                utilisateurAuteur.setDateCreation(new Date());
                this.utilisateurAuteurService.createUtilisateurAuteur(utilisateurAuteur);

                message = "Succès de création du auteur...";
                return ResponseEntity
                        .status(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(new ResponseMessage(message));
            }else{
                message = "Pas d'utilisateur approprié à cet ID...";
                return ResponseEntity
                        .status(HttpStatus.EXPECTATION_FAILED)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(new ResponseMessage(message));
            }

        } catch (Exception e){
            message = "Echec de création de cet auteur, svp bien remplir la forme...";
            return ResponseEntity
                    .status(HttpStatus.EXPECTATION_FAILED)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(new ResponseMessage(message));
        }

    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody List<Auteur> findAuteurs(){
        return this.auteurService.findAuteurs();
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody Auteur findAuteur(@PathVariable Integer id){
        return this.auteurService.findAuteur(id);
    }
}
