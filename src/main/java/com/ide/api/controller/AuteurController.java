package com.ide.api.controller;

import com.ide.api.dto.AuteurDTO;
import com.ide.api.entities.*;
import com.ide.api.enums.TypeGestion;
import com.ide.api.message.ResponseMessage;
import com.ide.api.repository.AuteurRepository;
import com.ide.api.service.AuteurService;
import com.ide.api.service.UtilisateurAuteurService;
import com.ide.api.service.UtilisateurService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.persistence.criteria.CriteriaBuilder;
import javax.validation.Valid;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping(path = "auteurs")
public class AuteurController {
    private AuteurService auteurService;
    private AuteurRepository auteurRepository;
    private UtilisateurService utilisateurService;
    private UtilisateurAuteurService utilisateurAuteurService;

    public AuteurController(AuteurService auteurService,
                            UtilisateurService utilisateurService,
                            UtilisateurAuteurService utilisateurAuteurService,
                            AuteurRepository auteurRepository) {
        this.auteurService = auteurService;
        this.utilisateurService = utilisateurService;
        this.utilisateurAuteurService = utilisateurAuteurService;
        this.auteurRepository = auteurRepository;
    }

    @ResponseStatus(value = HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(value="/admin/ajouter", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseMessage> createAuteur(@RequestBody AuteurDTO auteurDTO){
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Integer utilisateurID = userDetails.getId();
        Auteur auteur = new Auteur();
        auteur.setNom(auteurDTO.getNom());
        auteur.setPrenom(auteurDTO.getPrenom());
        auteur.setEmail(auteurDTO.getEmail());
        String message = "";
        try{
            Utilisateur utilisateur = this.utilisateurService.findUtilisateur(utilisateurID);
            auteur.setAuteurCreationAuteur(utilisateur.getUsername());
            if(utilisateur.isAdmin()){
                this.auteurService.createAuteur(auteur, utilisateurID);

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

    @GetMapping(value = "/public", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody List<Auteur> findAuteurs(){
        return this.auteurService.findAuteurs();
    }

    @GetMapping(value = "/public/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody Auteur findAuteur(@PathVariable Integer id){
        return this.auteurService.findAuteur(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/admin/update/{auteurID}")
    public ResponseEntity<Auteur> updateAuteur(@PathVariable Integer auteurID,
                                               @Valid @RequestBody Auteur auteurDetails){
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Integer adminID = userDetails.getId();
        Auteur auteur = this.auteurService.findAuteur(auteurID);
        Utilisateur utilisateur = this.utilisateurService.findUtilisateur(adminID);
        auteur.setNom(auteurDetails.getNom());
        auteur.setPrenom(auteurDetails.getPrenom());
        auteur.setEmail(auteurDetails.getEmail());
        final Auteur auteurUpdate = this.auteurRepository.save(auteur);
        UtilisateurAuteur newUtilAuteur = new UtilisateurAuteur();
        newUtilAuteur.setUtilisateurID(utilisateur);
        newUtilAuteur.setAuteurID(auteur);
        newUtilAuteur.setTypeGestion(TypeGestion.Modifier);
        utilisateurAuteurService.createUtilisateurAuteur(newUtilAuteur);
        return ResponseEntity.ok(auteurUpdate);
    }
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/admin/delete/{auteurID}")
    public ResponseEntity<Auteur> deleteAuteur(@PathVariable Integer auteurID){
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Integer adminID = userDetails.getId();
        Auteur auteur = this.auteurService.findAuteur(auteurID);
        Utilisateur utilisateur = this.utilisateurService.findUtilisateur(adminID);
        auteur.setSupprimerAuteur(true);
        final Auteur deletedAuteur = this.auteurRepository.save(auteur);
        UtilisateurAuteur newUtilAuteur = new UtilisateurAuteur();
        newUtilAuteur.setUtilisateurID(utilisateur);
        newUtilAuteur.setAuteurID(auteur);
        newUtilAuteur.setTypeGestion(TypeGestion.Supprimer);
        utilisateurAuteurService.createUtilisateurAuteur(newUtilAuteur);
        return ResponseEntity.ok(deletedAuteur);
    }
}
