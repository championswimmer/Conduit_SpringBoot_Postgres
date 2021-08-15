package com.scaler.conduit.controllers;

import com.scaler.conduit.dtos.responses.TagsResponse;
import com.scaler.conduit.entities.TagEntity;
import com.scaler.conduit.services.TagsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TagsController {
    private final TagsService tags;

    public TagsController(TagsService tags) {
        this.tags = tags;
    }

    @GetMapping("/tags")
    ResponseEntity<TagsResponse> getTags() {
        return ResponseEntity.ok(TagsResponse.fromTagEntities(tags.getAllTags()));
    }

}
