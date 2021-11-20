package com.scaler.conduit.dtos.requests;

import lombok.Getter;

@Getter
public class ArticleUpdateRequest {

    private Article article;

    @Getter
    public class Article {
        private String title;
        private String description;
        private String body;
    }
}
