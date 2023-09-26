package com.ide.api.controller;

import com.ide.api.entities.Auteur;
import com.ide.api.entities.Categorie;
import com.ide.api.service.AuteurService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping(path = "auteurs")
public class AuteurController {
    private AuteurService auteurService;

    public AuteurController(AuteurService auteurService) {
        this.auteurService = auteurService;
    }

    @ResponseStatus(value = HttpStatus.CREATED)
    @PostMapping(value="/ajouter", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void createAuteur(@RequestBody Auteur au){
        this.auteurService.createAuteur(au);
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
