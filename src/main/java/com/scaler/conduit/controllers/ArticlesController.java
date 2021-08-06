package com.scaler.conduit.controllers;

import com.scaler.conduit.dtos.requests.ArticleCreateRequest;
import com.scaler.conduit.dtos.responses.Article;
import com.scaler.conduit.dtos.responses.ArticleResponse;
import com.scaler.conduit.dtos.responses.AuthorResponse;
import com.scaler.conduit.entities.ArticleEntity;
import com.scaler.conduit.security.JWTAuthManager;
import com.scaler.conduit.services.ArticleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ArticlesController {
    private final ArticleService articles;
    private final JWTAuthManager jwtAuthManager;

    public ArticlesController(ArticleService articles, JWTAuthManager jwtAuthManager) {
        this.articles = articles;
        this.jwtAuthManager = jwtAuthManager;
    }

    @GetMapping("/articles")
    ResponseEntity<List<ArticleEntity>> getArticles(
            @RequestParam(value = "limit", required = false, defaultValue = "20") int limit,
            @RequestParam(value = "offset", required = false, defaultValue = "0") int offset
    ) {
        //Todo: convert to MultiArticleResponse
        return ResponseEntity.ok(articles.getAllArticles(limit, offset));
    }

    @PostMapping(value="/articles", consumes = "application/json", produces = "application/json")

    ArticleResponse createArticle(@RequestBody ArticleCreateRequest body) {
        var currentLoggedInUser = jwtAuthManager.getCurrentLoggedInUser();
        var articleEntity = articles.createArticle(body.getArticle().getTitle(), body.getArticle().getBody(), body.getArticle().getDescription(), body.getArticle().getTags(), currentLoggedInUser);
        var authorResponse = AuthorResponse.builder().bio(currentLoggedInUser.getBio()).username(currentLoggedInUser.getUsername()).image(currentLoggedInUser.getImage()).following(false).build();
        var articleResponse =  new ArticleResponse(Article.builder().slug(articleEntity.getSlug())
                 .title(articleEntity.getTitle())
                 .body(articleEntity.getBody())
                 .description(articleEntity.getDescription())
                 .tagList(body.getArticle().getTags())
                 .createdAt(articleEntity.getCreatedAt())
                 .updatedAt(articleEntity.getUpdatedAt())
                 .favoritesCount(0)
                 .favorited(false)
                 .author(authorResponse)
//                .favoritesCount(articleEntity.getFavorited().size())
//                .favorited(articleEntity.getFavorited().contains(articleEntity.getAuthor()))
                 .build());
         return articleResponse;
    }
}
