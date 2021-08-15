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
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ArticleResponseWithCount {
    long articlesCount;
    List<ArticleResponse> articles;


    public static ArticleResponseWithCount fromArticleEntity(List<ArticleEntity> articleEntities, long count) {
        return new ArticleResponseWithCount(
                count,
                articleEntities.stream().map(articleEntity -> {
                    return new ArticleResponse(ArticleResponse.Article.builder().slug(articleEntity.getSlug()).createdAt(articleEntity.getCreatedAt())
                            .updatedAt(articleEntity.getUpdatedAt()).title(articleEntity.getTitle()).
                                    body(articleEntity.getBody()).description(articleEntity.getDescription())
                            .tagList(articleEntity.getTags() != null ? articleEntity.getTags().stream().map(entity -> {
                                return entity.getName();
                            }).collect(Collectors.toList()) : null)
                            .author(articleEntity.getAuthor() != null ?
                                    ArticleResponse.Article.User.builder().username(articleEntity.getAuthor().getUsername())
                                            .bio(articleEntity.getAuthor().getBio()).image(articleEntity.getAuthor().getImage()).build() : null)
                            // set following
                            .build());
                }).collect(Collectors.toList())
        );
    }
}
