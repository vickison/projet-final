package com.ide.api.service;

import com.ide.api.entities.Tag;
import com.ide.api.repository.TagRepository;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
public class TagService {
    private TagRepository tagRepository;

    public TagService(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    public void createTag(Tag tag){
        this.tagRepository.save(tag);
    }

    public List<Tag> findTags(){
        return this.tagRepository.findAll();
    }

    public Tag findTag(Integer id){
        return this.tagRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Tag not found with id: " + id));
    }
}
