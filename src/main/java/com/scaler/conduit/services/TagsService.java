package com.scaler.conduit.services;

import com.scaler.conduit.entities.TagEntity;
import com.scaler.conduit.repositories.TagRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TagsService {
    private final TagRepository tagsRepo;

    public TagsService(TagRepository tagsRepo) {
        this.tagsRepo = tagsRepo;
    }

    public List<TagEntity> getAllTags() {
        return tagsRepo.findAll();
    }

    public TagEntity createTag(String tag) {
        return tagsRepo.save(new TagEntity(tag));
    }

    public List<TagEntity> createTags(List<String> tags) {
        List<TagEntity> tagEntities = new ArrayList<>();
        for (String tag : tags) {
            if(!tagsRepo.findByName(tag).isPresent())
                this.createTag(tag);
            tagEntities.add(tagsRepo.findByName(tag).get());
        }
        return tagEntities;
    }
}
