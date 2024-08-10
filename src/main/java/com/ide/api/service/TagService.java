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
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TagService {

    private static final Logger logger = LoggerFactory.getLogger(TagService.class);
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

    @Transactional
    public void createTag(Tag tag, Integer idUtilisateur) {
        Logger logger = LoggerFactory.getLogger(getClass());

        if (tag == null) {
            logger.warn("Le tag fourni est nul.");
            throw new IllegalArgumentException("Le tag ne peut pas être nul.");
        }

        if (idUtilisateur == null) {
            logger.warn("L'identifiant utilisateur fourni est nul.");
            throw new IllegalArgumentException("L'identifiant utilisateur ne peut pas être nul.");
        }

        try {
            Tag tagSaved = this.tagRepository.save(tag);
            Utilisateur utilisateur = this.utilisateurRepository.findById(idUtilisateur)
                    .orElseThrow(() -> new EntityNotFoundException("Utilisateur avec identifiant: " + idUtilisateur + " introuvable"));

            UtilisateurTag utilisateurTag = new UtilisateurTag();
            utilisateurTag.setUtilisateur(utilisateur);
            utilisateurTag.setTag(tagSaved);
            utilisateurTag.setTypeGestion(TypeGestion.Ajouter);

            this.utilisateurTagRepository.save(utilisateurTag);

        } catch (DataAccessException e) {
            logger.error("Erreur d'accès aux données lors de la création du tag pour l'utilisateur avec l'identifiant: {}", idUtilisateur, e);
            throw new RuntimeException("Erreur lors de la création du tag pour l'utilisateur avec l'identifiant: " + idUtilisateur, e);
        }
    }

    public List<Tag> findTags() {
        try {
            List<Tag> tags = this.tagRepository.findAll();
            logger.info("Nombre de tags trouvés: {}", tags.size());
            return tags;
        } catch (DataAccessException e) {
            logger.error("Erreur lors de la récupération des tags", e);
            throw new RuntimeException("Erreur lors de la récupération des tags", e);
        }
    }

    public Tag findTag(Integer id) {
        try {
            Tag tag = this.tagRepository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("Tag avec identifiant: " + id + " introuvable"));
            logger.info("Tag trouvé: {}", tag);
            return tag;
        } catch (EntityNotFoundException e) {
            logger.error("Erreur lors de la recherche du Tag avec identifiant: {}", id, e);
            throw e; // Rejeter l'exception après l'avoir loguée
        } catch (Exception e) {
            logger.error("Erreur inattendue lors de la recherche du Tag avec identifiant: {}", id, e);
            throw new RuntimeException("Erreur inattendue lors de la recherche du Tag", e);
        }
    }

    public List<Tag> findTagsByDocumentId(Document document) {
        if (document == null) {
            logger.error("Document fourni est null");
            throw new IllegalArgumentException("Le document ne peut pas être null");
        }

        try {
            List<Tag> tags = this.tagRepository.findByDocumentTagsDocumentID(document);
            logger.info("Tags trouvés pour le document ID {}: {}", document.getDocumentID(), tags);
            return tags;
        } catch (Exception e) {
            logger.error("Erreur lors de la recherche des Tags pour le document ID {}", document.getDocumentID(), e);
            throw new RuntimeException("Erreur lors de la recherche des Tags", e);
        }
    }

    public List<Tag> findTagsByUtilisateurId(Utilisateur utilisateur) {
        if (utilisateur == null) {
            logger.error("Utilisateur fourni est null");
            throw new IllegalArgumentException("L'utilisateur ne peut pas être null");
        }

        try {
            List<Tag> tags = this.tagRepository.findByUtilisateurTagsUtilisateurID(utilisateur);
            logger.info("Tags trouvés pour l'utilisateur ID {}: {}", utilisateur.getUtilisateurID(), tags);
            return tags;
        } catch (Exception e) {
            logger.error("Erreur lors de la recherche des Tags pour l'utilisateur ID {}", utilisateur.getUtilisateurID(), e);
            throw new RuntimeException("Erreur lors de la recherche des Tags", e);
        }
    }
}
