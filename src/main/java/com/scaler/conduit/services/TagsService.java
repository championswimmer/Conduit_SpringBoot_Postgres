package com.scaler.conduit.services;

import com.scaler.conduit.entities.TagEntity;
import com.scaler.conduit.repositories.TagRepository;
import org.springframework.stereotype.Service;

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

    public TagEntity saveTag(String tagName) {
        TagEntity tagEntity = new TagEntity(tagName);
        return tagsRepo.save(tagEntity);
    }
}
