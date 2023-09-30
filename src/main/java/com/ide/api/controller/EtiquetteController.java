package com.ide.api.controller;

import com.ide.api.entities.Admin;
import com.ide.api.entities.Etiquette;
import com.ide.api.repository.EtiquetteRepository;
import com.ide.api.service.EtiquetteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping(path = "etiquettes")
public class EtiquetteController {
    private EtiquetteService etiquetteService;

    public EtiquetteController(EtiquetteService etiquetteService) {
        this.etiquetteService = etiquetteService;
    }

    @ResponseStatus(value = HttpStatus.CREATED)
    @PostMapping(value="/ajouter", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void createEtiquette(@RequestBody Etiquette et){
        this.etiquetteService.createEtiquette(et);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody List<Etiquette> findAllEtiquette(){
        return this.findAllEtiquette();
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody Etiquette findEtiquette(@PathVariable Integer id){
        return this.etiquetteService.findEtiquette(id);
    }
}
