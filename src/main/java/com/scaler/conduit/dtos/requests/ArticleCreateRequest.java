package com.scaler.conduit.dtos.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.scaler.conduit.entities.TagEntity;
import lombok.Getter;

import java.util.List;

@Getter
public class ArticleCreateRequest {
    private Article article;

    @Getter
    public class Article {
        private String title;
        private String description;
        private String body;
        @JsonProperty("tagList")
        private List<String> tags;
    }
}
