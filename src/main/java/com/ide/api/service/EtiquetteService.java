package com.ide.api.service;

import com.ide.api.entities.Admin;
import com.ide.api.entities.Etiquette;
import com.ide.api.repository.EtiquetteRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EtiquetteService {
    private EtiquetteRepository etiquetteRepository;

    public EtiquetteService(EtiquetteRepository etiquetteRepository) {
        this.etiquetteRepository = etiquetteRepository;
    }

    public void createEtiquette(Etiquette etiquette){
        this.etiquetteRepository.save(etiquette);
    }

    public List<Etiquette> findAllEtiquette(){
        return this.etiquetteRepository.findAll();
    }

    public Etiquette findEtiquette(Integer id){
        return this.etiquetteRepository.findById(id).get();
    }
}
