package com.scaler.conduit.controllers;

import com.scaler.conduit.converters.ArticleObjectConverter;
import com.scaler.conduit.converters.CommentObjectConverter;
import com.scaler.conduit.dtos.requests.ArticleCreateRequest;
import com.scaler.conduit.dtos.requests.ArticleUpdateRequest;
import com.scaler.conduit.dtos.requests.CommentCreateRequest;
import com.scaler.conduit.dtos.responses.ArticleResponse;
import com.scaler.conduit.dtos.responses.Comment;
import com.scaler.conduit.dtos.responses.CommentResponse;
import com.scaler.conduit.entities.ArticleEntity;
import com.scaler.conduit.entities.CommentEntity;
import com.scaler.conduit.entities.ErrorEntity;
import com.scaler.conduit.entities.UserEntity;
import com.scaler.conduit.services.ArticleService;
import com.scaler.conduit.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class ArticlesController {
    private final ArticleService articles;
    ArticleObjectConverter articleObjectConverter;
    CommentObjectConverter commentObjectConverter;

    public ArticlesController(
            ArticleService articles,
            ArticleObjectConverter articleObjectConverter,
            CommentObjectConverter commentObjectConverter) {
        this.articles = articles;
        this.articleObjectConverter = articleObjectConverter;
        this.commentObjectConverter = commentObjectConverter;
    }

    @GetMapping(value = "/articles", produces = "application/json")
    ResponseEntity<List<ArticleResponse>> getArticleList(
            @RequestParam(value = "tag", required = false, defaultValue = "10") String tag,
            @RequestParam(value = "author", required = false, defaultValue = "0") String author,
            @RequestParam(value = "favorited", required = false, defaultValue = "10") String favorited,
            @RequestParam(value = "offset", required = false, defaultValue = "0") int offset,
            @RequestParam(value = "limit", required = false, defaultValue = "10") int limit
    ) {
        var articleEntities = articles.getAllArticleList(tag, author, favorited, limit, offset);
        List<ArticleResponse> articleResponseList = new ArrayList<>();
        articleEntities.forEach(article -> articleResponseList.add(articleObjectConverter.entityToResponse(article)));
        return ResponseEntity.ok(articleResponseList);
    }

    @GetMapping(value = "articles/feed", produces = "application/json")
    ResponseEntity<List<ArticleResponse>> feedArticles(
            @RequestParam(value = "limit", required = false, defaultValue = "10") int limit,
            @RequestParam(value = "offset", required = false, defaultValue = "0") int offset,
            @AuthenticationPrincipal UserEntity userEntity
    ) {
        var article = articles.getArticleFeed(userEntity, limit, offset);
        List<ArticleResponse> articleResponseList = article.stream()
                .map(x -> articleObjectConverter.entityToResponse(x))
                .collect(Collectors.toList());
        return ResponseEntity.ok(articleResponseList);
    }

    @GetMapping(value = "/articles/{slug}", produces = "application/json")
    ResponseEntity<ArticleResponse> getArticleBySlug(@PathVariable(value = "slug") String slug) {

        var article = articles.getArticleBySlug(slug);
        return ResponseEntity.ok(articleObjectConverter.entityToResponse(article));
    }

    @PostMapping(value = "/articles", consumes = "application/json", produces = "application/json")
    ResponseEntity<ArticleResponse> createArticle(
            @RequestBody ArticleCreateRequest body,
            @AuthenticationPrincipal UserEntity userEntity
    ) {
        var articleEntity = articles.createArticle(
                body.getArticle().getTitle(),
                body.getArticle().getBody(),
                body.getArticle().getDescription(),
                body.getArticle().getTags(),
                userEntity
        );
        return ResponseEntity.ok(articleObjectConverter.entityToResponse(articleEntity));
    }


    @PutMapping(value = "/articles/{slug}", produces = "application/json")
    ResponseEntity<ArticleResponse> updateArticle(
            @PathVariable(value = "slug", required = true) String slug,
            @RequestBody ArticleUpdateRequest body,
            @AuthenticationPrincipal UserEntity userEntity
    ) {
        ArticleEntity article = articles.updateArticle(body.getArticle(), slug, userEntity);
        return ResponseEntity.ok(articleObjectConverter.entityToResponse(article));
    }


    @DeleteMapping(value = "/articles/{slug}", produces = "application/json")
    ResponseEntity<String> deleteArticle(
            @PathVariable(value = "slug", required = true) String slug,
            @AuthenticationPrincipal UserEntity userEntity
    ) {
        articles.deleteArticleBySlug(slug, userEntity);
        return ResponseEntity.ok("Article Deleted");
    }

    @PostMapping(value = "/articles/{slug}/comments", produces = "application/json")
    ResponseEntity<CommentResponse> addCommentToArticle(
            @PathVariable(value = "slug", required = true) String slug,
            @RequestBody CommentCreateRequest body,
            @AuthenticationPrincipal UserEntity userEntity
    ) {
        CommentEntity response = articles.addCommentToArticle(slug, body.getComment(), userEntity);
        return ResponseEntity.ok(commentObjectConverter.entityToResponse(response));
    }

    @GetMapping(value = "/articles/{slug}/comments", produces = "application/json")
    ResponseEntity<List<Comment>> getCommentsFromArticle(
            @PathVariable(value = "slug", required = true) String slug
    ) {
        List<CommentEntity> comments = articles.getAllCommentBySlug(slug);
        return ResponseEntity.ok(commentObjectConverter.entityToResponse(comments));
    }

    @DeleteMapping(value = "/articles/{slug}/comments/{id}", produces = "application/json")
    ResponseEntity<String> deleteComment(
            @PathVariable(value = "slug", required = true) String slug,
            @PathVariable(value = "id", required = true) long id,
            @AuthenticationPrincipal UserEntity userEntity
    ) {
        articles.deleteComment(id, userEntity);
        return ResponseEntity.ok("Comment deleted successfully.");
    }

    @PostMapping(value = "/articles/{slug}/favorite", produces = "application/json")
    ResponseEntity<ArticleResponse> favoriteArticle(
            @PathVariable(value = "slug", required = true) String slug,
            @AuthenticationPrincipal UserEntity userEntity
    ) {
        ArticleEntity articleEntity = articles.favoriteArticle(slug, userEntity);
        return ResponseEntity.ok(articleObjectConverter.entityToResponse(articleEntity));
    }

    @DeleteMapping(value = "/articles/{slug}/favorite", produces = "application/json")
    ResponseEntity<ArticleResponse> unfavoriteArticle(
            @PathVariable(value = "slug", required = true) String slug,
            @AuthenticationPrincipal UserEntity userEntity
    ) {
        ArticleEntity articleEntity = articles.unfavoriteArticle(slug, userEntity);
        return ResponseEntity.ok(articleObjectConverter.entityToResponse(articleEntity));
    }


    @ExceptionHandler({RuntimeException.class})
    ResponseEntity<ErrorEntity> handleExceptions(RuntimeException exception) {
        String message = exception.getMessage();
        HttpStatus errorStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        if (exception instanceof ArticleService.AccessDeniedException) {
            errorStatus = HttpStatus.BAD_REQUEST;
        }

        return new ResponseEntity<>(
                ErrorEntity.from(message), errorStatus
        );
    }
}
