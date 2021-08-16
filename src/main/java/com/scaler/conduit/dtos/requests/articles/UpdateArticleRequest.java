package com.scaler.conduit.dtos.requests.articles;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;


public class UpdateArticleRequest {
    @JsonProperty("article")
    Article article;

    @Getter
    public class Article {
        @JsonProperty("title")
        String title;
        @JsonProperty("description")
        String description;
        @JsonProperty("body")
        String body;
        @JsonProperty("tagList")
        String[] tagList;
    }
}
