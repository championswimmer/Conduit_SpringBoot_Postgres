package com.scaler.conduit.dtos.responses;

import lombok.Builder;
import lombok.Getter;

import java.util.Date;
import java.util.List;

@Builder
@Getter
public class Article {
    private String slug;
    private String title;
    private String description;
    private String body;
    private List<String> tagList;
    private Date createdAt;
    private Date updatedAt;
    private boolean favorited;
    private int favoritesCount;
    private AuthorResponse author;

}
