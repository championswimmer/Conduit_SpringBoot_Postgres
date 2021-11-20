package com.scaler.conduit.converters;

import com.scaler.conduit.dtos.responses.Article;
import com.scaler.conduit.dtos.responses.ArticleResponse;
import com.scaler.conduit.dtos.responses.AuthorResponse;
import com.scaler.conduit.entities.ArticleEntity;
import com.scaler.conduit.entities.TagEntity;
import com.scaler.conduit.entities.UserEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ArticleObjectConverter {
    public ArticleResponse entityToResponse(ArticleEntity articleEntity) {
        List<String> tagNames = new ArrayList<>();
        articleEntity.getTags().forEach(tag -> {
            tagNames.add(tag.getName());
        });

        UserEntity author = articleEntity.getAuthor();
        var authorResponse = AuthorResponse.builder()
                .bio(author.getBio())
                .username(author.getUsername())
                .image(author.getImage())
                .following(false)
                .build();

        return new ArticleResponse(Article.builder().slug(articleEntity.getSlug())
                .title(articleEntity.getTitle())
                .body(articleEntity.getBody())
                .description(articleEntity.getDescription())
                .tagList(tagNames)
                .createdAt(articleEntity.getCreatedAt())
                .updatedAt(articleEntity.getUpdatedAt())
                .favoritesCount(0)
                .favorited(false)
                .author(authorResponse)
//                .favoritesCount(articleEntity.getFavorited().size())
//                .favorited(articleEntity.getFavorited().contains(articleEntity.getAuthor()))
                .build());
    }

}
