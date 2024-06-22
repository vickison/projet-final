package com.ide.api.service;

import com.ide.api.dto.TagDTO;
import com.ide.api.entities.Document;
import com.ide.api.entities.Tag;
import com.ide.api.entities.Utilisateur;
import com.ide.api.entities.UtilisateurTag;
import com.ide.api.enums.TypeGestion;
import com.ide.api.repository.TagRepository;
import com.ide.api.repository.UtilisateurRepository;
import com.ide.api.repository.UtilisateurTagRepository;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
public class TagService {
    private TagRepository tagRepository;
    private UtilisateurRepository utilisateurRepository;
    private UtilisateurTagRepository utilisateurTagRepository;

    public TagService(TagRepository tagRepository,
                      UtilisateurRepository utilisateurRepository,
                      UtilisateurTagRepository utilisateurTagRepository) {
        this.tagRepository = tagRepository;
        this.utilisateurRepository = utilisateurRepository;
        this.utilisateurTagRepository = utilisateurTagRepository;
    }

    public void createTag(Tag tag,
                          Integer idUtilisateur){
        Tag tagSaved = this.tagRepository.save(tag);
        UtilisateurTag utilisateurTag = new UtilisateurTag();
        Utilisateur utilisateur = this.utilisateurRepository.findById(idUtilisateur)
                .orElseThrow(() -> new EntityNotFoundException("Utilisateur avec identifiant: " + idUtilisateur + " introuvable"));
        utilisateurTag.setUtilisateur(utilisateur);
        utilisateurTag.setTag(tagSaved);
        utilisateurTag.setTypeGestion(TypeGestion.Ajouter);
        this.utilisateurTagRepository.save(utilisateurTag);
    }

    public List<Tag> findTags(){
        return this.tagRepository.findAll();
    }

    public Tag findTag(Integer id){
        return this.tagRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Etiquette avec identifiant: " + id + " introuvable"));
    }

    public List<Tag> findTagsByDocumentId(Document document){
        return this.tagRepository.findByDocumentTagsDocumentID(document);
    }

    public List<Tag> findTagsByUtilisateurId(Utilisateur utilisateur){
        return this.tagRepository.findByUtilisateurTagsUtilisateurID(utilisateur);
    }
}
