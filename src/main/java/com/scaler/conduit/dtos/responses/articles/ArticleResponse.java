package com.scaler.conduit.dtos.responses.articles;

import com.scaler.conduit.entities.ArticleEntity;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public class ArticleResponse {
    Article article;

    @Getter
    @Builder
   public static class Article {
        String slug;
        String title;
        String description;
        String body;
        List<String> tagList;
        Date createdAt;
        Date updatedAt;
        boolean favorited;
        long favoritesCount;
        User author;


        @Getter
        @Builder
        static class User {
            String username;
            String bio;
            String image;
            boolean following;
        }
    }

    public static ArticleResponse fromArticleEntity(ArticleEntity articleEntity) {
        return new ArticleResponse(
                Article.builder().slug(articleEntity.getSlug()).createdAt(articleEntity.getCreatedAt())
                        .updatedAt(articleEntity.getUpdatedAt()).title(articleEntity.getTitle()).
                        body(articleEntity.getBody()).description(articleEntity.getDescription())
                        .tagList(articleEntity.getTags() != null ? articleEntity.getTags().stream().map(entity -> {
                            return entity.getName();
                        }).collect(Collectors.toList()) : null)
                        .author(articleEntity.getAuthor() != null ?
                                Article.User.builder().username(articleEntity.getAuthor().getUsername())
                                        .bio(articleEntity.getAuthor().getBio()).image(articleEntity.getAuthor().getImage()).build() : null)
                        // set following
                        .build()
        );
    }
}
