package com.ide.api.controller;

import com.ide.api.dto.TagDTO;
import com.ide.api.entities.*;
import com.ide.api.enums.TypeGestion;
import com.ide.api.message.ResponseMessage;
import com.ide.api.repository.DocumentTagRepository;
import com.ide.api.repository.TagRepository;
import com.ide.api.repository.UtilisateurRepository;
import com.ide.api.repository.UtilisateurTagRepository;
import com.ide.api.service.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping(path = "tags")
public class TagController {
    private TagService tagService;
    private TagRepository tagRepository;
    private UtilisateurService utilisateurService;
    private DocumentService documentService;
    private UtilisateurTagService utilisateurTagService;
    private DocumentTagService documentTagService;
    private UtilisateurTagRepository utilisateurTagRepository;
    private DocumentTagRepository documentTagRepository;
    private UtilisateurRepository utilisateurRepository;

    public TagController(TagService tagService,
                         UtilisateurService utilisateurService,
                         DocumentService documentService,
                         UtilisateurTagService utilisateurTagService,
                         DocumentTagService documentTagService,
                         UtilisateurTagRepository utilisateurTagRepository,
                         DocumentTagRepository documentTagRepository,
                         TagRepository tagRepository,
                         UtilisateurRepository utilisateurRepository) {
        this.tagService = tagService;
        this.utilisateurService = utilisateurService;
        this.documentService = documentService;
        this.utilisateurTagService = utilisateurTagService;
        this.documentTagService = documentTagService;
        this.utilisateurTagRepository = utilisateurTagRepository;
        this.documentTagRepository = documentTagRepository;
        this.tagRepository = tagRepository;
        this.utilisateurRepository = utilisateurRepository;
    }

    @ResponseStatus(value = HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(value="/admin/ajouter")
    public ResponseEntity<ResponseMessage> createTag(@RequestBody TagDTO tagDTO){
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Integer utilisateurID = userDetails.getId();
        System.out.println(utilisateurID);
        Tag tag = new Tag();
        tag.setTag(tagDTO.getTag());
        String message = "";
        try{
            Utilisateur utilisateur = this.utilisateurService.findUtilisateur(utilisateurID);
            tag.setAdminCreationEtiquette(utilisateur.getUsername());
            if(utilisateur.isAdmin()){
                this.tagService.createTag(tag, utilisateurID);
                message = "Succès de création du tag...";
                return ResponseEntity
                        .status(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(new ResponseMessage(message));
            }else{
                message = "Pas d'utilisateur approprié...";
                return ResponseEntity
                        .status(HttpStatus.EXPECTATION_FAILED)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(new ResponseMessage(message));
            }

        }catch (Exception e){
            message = "Echec de création du tag...";
            return ResponseEntity
                    .status(HttpStatus.EXPECTATION_FAILED)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(new ResponseMessage(message));
        }
    }

    @GetMapping(value = "/public", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody List<Tag> findTags(){
        return this.tagService.findTags();
    }

    @GetMapping(value = "/public/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody Tag findTag(@PathVariable Integer id){
        return this.tagService.findTag(id);
    }

    @GetMapping("/public/{tagID}/documents")
    public ResponseEntity<List<Document>> findDocumentsByTagId(@PathVariable Integer tagID){
        Tag tag = this.tagService.findTag(tagID);
        List<Document> documents = this.documentService.findDocumentsByTagId(tag);
        return ResponseEntity.ok(documents);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/admin/update/{tagID}")
    public ResponseEntity<Tag> updateTag(@PathVariable Integer tagID,
                                         @Valid @RequestBody Tag tagDetails){
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Integer adminID = userDetails.getId();
        Tag tag = this.tagService.findTag(tagID);
        Utilisateur utilisateur = this.utilisateurService.findUtilisateur(adminID);
        tag.setTag(tagDetails.getTag());
        tag.setAdminModificationEtiquette(utilisateur.getUsername());
        final Tag updatedTag = this.tagRepository.save(tag);
        Optional<UtilisateurTag> utilTag = this.utilisateurTagService.findByTagAndUtil(updatedTag, utilisateur);
        if(utilTag.isPresent()){
            UtilisateurTag utilisateurTag = utilTag.get();
            utilisateurTag.setTypeGestion(TypeGestion.Modifier);
            this.utilisateurTagService.createUtilisateurTag(utilisateurTag);
            System.out.println("Inside condition");
        }else {
            UtilisateurTag newUtilTag = new UtilisateurTag();
            newUtilTag.setUtilisateurID(utilisateur);
            newUtilTag.setTagID(updatedTag);
            newUtilTag.setTypeGestion(TypeGestion.Modifier);
            utilisateurTagService.createUtilisateurTag(newUtilTag);
            System.out.println("Not inside condition");
        }
        return ResponseEntity.ok(updatedTag);
    }
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/admin/delete/{tagID}")
    public ResponseEntity<Tag> deleteTag(@PathVariable Integer tagID){
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Integer adminID = userDetails.getId();
        Tag tag = this.tagService.findTag(tagID);
        Utilisateur utilisateur = this.utilisateurService.findUtilisateur(adminID);
        tag.setSupprimerEtiquette(true);
        tag.setAdminModificationEtiquette(utilisateur.getUsername());
        final Tag deletedTag = this.tagRepository.save(tag);
        Optional<UtilisateurTag> utilTag = this.utilisateurTagService.findByTagAndUtil(deletedTag, utilisateur);
        if(utilTag.isPresent()){
            UtilisateurTag utilisateurTag = utilTag.get();
            utilisateurTag.setTypeGestion(TypeGestion.Supprimer);
            this.utilisateurTagService.createUtilisateurTag(utilisateurTag);
            System.out.println("Inside condition");
        }else {
            UtilisateurTag newUtilTag = new UtilisateurTag();
            newUtilTag.setUtilisateurID(utilisateur);
            newUtilTag.setTagID(deletedTag);
            newUtilTag.setTypeGestion(TypeGestion.Supprimer);
            utilisateurTagService.createUtilisateurTag(newUtilTag);
            System.out.println("Not inside condition");
        }
        return ResponseEntity.ok(deletedTag);
    }


//    private Integer extractUtilisateurIdFromToken(Authentication authentication){
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        CustomUserDetails customUser = (CustomUserDetails) authentication.getPrincipal();
//        return customUser.getUtilisateurID();
//    }


    private Integer getUtilisateurIdFromDatabase(String username){
        return utilisateurRepository.findByUsername(username).getUtilisateurID();
    }
}
