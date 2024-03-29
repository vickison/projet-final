package com.ide.api.controller;

import com.ide.api.entities.*;
import com.ide.api.message.ResponseMessage;
import com.ide.api.repository.DocumentTagRepository;
import com.ide.api.repository.TagRepository;
import com.ide.api.repository.UtilisateurTagRepository;
import com.ide.api.service.*;
import org.springframework.boot.context.config.ConfigDataResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;

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

    public TagController(TagService tagService,
                         UtilisateurService utilisateurService,
                         DocumentService documentService,
                         UtilisateurTagService utilisateurTagService,
                         DocumentTagService documentTagService,
                         UtilisateurTagRepository utilisateurTagRepository,
                         DocumentTagRepository documentTagRepository,
                         TagRepository tagRepository) {
        this.tagService = tagService;
        this.utilisateurService = utilisateurService;
        this.documentService = documentService;
        this.utilisateurTagService = utilisateurTagService;
        this.documentTagService = documentTagService;
        this.utilisateurTagRepository = utilisateurTagRepository;
        this.documentTagRepository = documentTagRepository;
        this.tagRepository = tagRepository;
    }

    @ResponseStatus(value = HttpStatus.CREATED)
    @PostMapping(value="/ajouter", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseMessage> createTag(@RequestBody Tag tag,
                                                     @RequestParam(value = "U", required = true) Integer utilisateurID){
        String message = "";
        try{
            Utilisateur utilisateur = this.utilisateurService.findUtilisateur(utilisateurID);
            if(utilisateur.isAdmin()){
                this.tagService.createTag(tag);

                UtilisateurTag utilisateurTag = new UtilisateurTag();
                utilisateurTag.setUtilisateur(utilisateur);
                utilisateurTag.setDateCreation(new Date());
                utilisateurTag.setTag(tag);
                this.utilisateurTagService.createUtilisateurTag(utilisateurTag);
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

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody List<Tag> findTags(){
        return this.tagService.findTags();
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody Tag findTag(@PathVariable Integer id){
        return this.tagService.findTag(id);
    }

    @GetMapping("/{tagID}/documents")
    public ResponseEntity<List<Document>> findDocumentsByTagId(@PathVariable Integer tagID){
        Tag tag = this.tagService.findTag(tagID);
        List<Document> documents = this.documentService.findDocumentsByTagId(tag);
        return ResponseEntity.ok(documents);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Tag> updateTag(@PathVariable Integer id,
                                         @Valid @RequestBody Tag tagDetails){
        Tag tag = this.tagService.findTag(id);
        tag.setTag(tagDetails.getTag());
        final Tag updatedTag = this.tagRepository.save(tag);
        return ResponseEntity.ok(updatedTag);
    }
}
