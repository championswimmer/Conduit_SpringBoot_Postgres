package com.scaler.conduit.controllers;

import com.scaler.conduit.entities.ArticleEntity;
import com.scaler.conduit.services.ArticleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ArticlesController {
    private ArticleService articles;

    public ArticlesController(ArticleService articles) {
        this.articles = articles;
    }

    @GetMapping("/articles")
    ResponseEntity<List<ArticleEntity>> getArticles(
            @RequestParam(value = "limit", required = false, defaultValue = "20") int limit,
            @RequestParam(value = "offset", required = false, defaultValue = "0") int offset
    ) {
        return ResponseEntity.ok(articles.getAllArticles(limit, offset));
    }
}
