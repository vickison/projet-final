package com.ide.api.service;

import com.ide.api.entities.UtilisateurTag;
import com.ide.api.repository.UtilisateurTagRepository;
import org.springframework.stereotype.Service;

@Service
public class UtilisateurTagService {
    private UtilisateurTagRepository utilisateurTagRepository;

    public UtilisateurTagService(UtilisateurTagRepository utilisateurTagRepository) {
        this.utilisateurTagRepository = utilisateurTagRepository;
    }

    public void createUtilisateurTag(UtilisateurTag utilisateurTag){
        this.utilisateurTagRepository.save(utilisateurTag);
    }
}
