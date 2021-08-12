package com.scaler.conduit.controllers;

import com.scaler.conduit.converters.ArticleObjectConverter;
import com.scaler.conduit.dtos.requests.ArticleCreateRequest;
import com.scaler.conduit.dtos.responses.ArticleResponse;
import com.scaler.conduit.entities.UserEntity;
import com.scaler.conduit.services.ArticleService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class ArticlesController {
    private final ArticleService articles;
    ArticleObjectConverter articleObjectConverter;

    public ArticlesController(ArticleService articles, ArticleObjectConverter articleObjectConverter) {
        this.articles = articles;
        this.articleObjectConverter = articleObjectConverter;
    }

    @GetMapping("/articles")
    ResponseEntity<List<ArticleResponse>> getArticles(
            @RequestParam(value = "limit", required = false, defaultValue = "20") int limit,
            @RequestParam(value = "offset", required = false, defaultValue = "0") int offset
    ) {
        //Todo: Convert to MultiArticleResponse and handle this request based on the authentication context.
        List<ArticleResponse> articleResponseList = new ArrayList<>();
        var articleEntities = articles.getAllArticles(limit, offset);
        articleEntities.forEach(article -> articleResponseList.add(articleObjectConverter.entityTorResponse(article)));
        return ResponseEntity.ok(articleResponseList);
    }

    @PostMapping(value = "/articles", consumes = "application/json", produces = "application/json")
    ResponseEntity<ArticleResponse> createArticle(@RequestBody ArticleCreateRequest body, @AuthenticationPrincipal UserEntity userEntity) {
        var articleEntity = articles.createArticle(
                body.getArticle().getTitle(),
                body.getArticle().getBody(),
                body.getArticle().getDescription(),
                body.getArticle().getTags(),
                userEntity
        );
        return ResponseEntity.ok(articleObjectConverter.entityTorResponse(articleEntity));
    }
}
