package com.ide.api.controller;

import com.ide.api.entities.Tag;
import com.ide.api.service.TagService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping(path = "tags")
public class TagController {
    private TagService tagService;

    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    @ResponseStatus(value = HttpStatus.CREATED)
    @PostMapping(value="/ajouter", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void createEtiquette(@RequestBody Tag tag){
        this.tagService.createTag(tag);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody List<Tag> findTags(){
        return this.tagService.findTags();
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody Tag findTag(@PathVariable Integer id){
        return this.tagService.findTag(id);
    }
}
