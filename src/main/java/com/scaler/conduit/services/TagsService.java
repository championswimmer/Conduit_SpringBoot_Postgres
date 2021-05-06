package com.scaler.conduit.services;

import com.scaler.conduit.entities.TagEntity;
import com.scaler.conduit.repositories.TagRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TagsService {
    private TagRepository tagsRepo;

    public TagsService(TagRepository tagsRepo) {
        this.tagsRepo = tagsRepo;
    }

    public List<TagEntity> getAllTags() {
        return tagsRepo.findAll();
    }
}
