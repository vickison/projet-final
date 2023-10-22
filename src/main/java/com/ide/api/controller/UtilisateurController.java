package com.ide.api.controller;

import com.ide.api.entities.Utilisateur;
import com.ide.api.message.ResponseMessage;
import com.ide.api.service.UtilisateurService;
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
@RequestMapping(path = "utilisateur")
public class UtilisateurController {
    private UtilisateurService utilisateurService;

    public UtilisateurController(UtilisateurService utilisateurService) {
        this.utilisateurService = utilisateurService;
    }


    @ResponseStatus(value = HttpStatus.CREATED)
    @PostMapping(value="/ajouter", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseMessage> createUser(@RequestBody Utilisateur utilisateur){
        String message = "";
        try{
            if(EmailValidator.isValid(utilisateur.getEmail())) {
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

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody List<Utilisateur> findUsers(){
        return this.utilisateurService.findUtilisateurs();
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody Utilisateur findUser(@PathVariable Integer id){
        return this.utilisateurService.findUtilisateur(id);
    }
}
