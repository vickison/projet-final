package com.ide.api.service;

import com.ide.api.entities.Auteur;
import com.ide.api.entities.Tag;
import com.ide.api.entities.Utilisateur;
import com.ide.api.entities.UtilisateurTag;
import com.ide.api.enums.TypeGestion;
import com.ide.api.repository.UtilisateurTagRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UtilisateurTagService {
    private UtilisateurTagRepository utilisateurTagRepository;

    public UtilisateurTagService(UtilisateurTagRepository utilisateurTagRepository) {
        this.utilisateurTagRepository = utilisateurTagRepository;
    }

    public void createUtilisateurTag(UtilisateurTag utilisateurTag){
        this.utilisateurTagRepository.save(utilisateurTag);
    }

    public UtilisateurTag findUtilTagByTagID(Tag tag){
        UtilisateurTag utilisateurTag = new UtilisateurTag();
        List<UtilisateurTag> utilisateurTags = this.utilisateurTagRepository.findByTagID(tag);
        for(UtilisateurTag ut: utilisateurTags){
            if(ut.getTypeGestion() == TypeGestion.Ajouter){
                utilisateurTag = ut;
            }
        }
        return utilisateurTag;
    }

    public Optional<UtilisateurTag> findByTagAndUtil(Tag tag, Utilisateur utilisateur){
        return this.utilisateurTagRepository.findByTagIDAndUtilisateurID(tag, utilisateur);
    }
}
