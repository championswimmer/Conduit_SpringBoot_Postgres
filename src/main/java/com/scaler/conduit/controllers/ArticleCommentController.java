package com.scaler.conduit.controllers;

import com.scaler.conduit.dtos.requests.ArticleCommentRequest;
import com.scaler.conduit.dtos.responses.comments.ArticleCommentResponse;
import com.scaler.conduit.dtos.responses.comments.ArticleCommentResponseWithCount;
import com.scaler.conduit.entities.ArticleEntity;
import com.scaler.conduit.entities.CommentEntity;
import com.scaler.conduit.entities.ErrorEntity;
import com.scaler.conduit.entities.UserEntity;
import com.scaler.conduit.services.ArticleCommentService;
import com.scaler.conduit.services.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ArticleCommentController {

    @Autowired
    ArticleCommentService articleCommentService;

    @Autowired
    ArticleService articleService;

    @PostMapping("/articles/{slug}/comments")
    public ResponseEntity<ArticleCommentResponse> addCommentInArticle(@AuthenticationPrincipal UserEntity userEntity, @PathVariable(value = "slug") String slug,
                                                                      @RequestBody ArticleCommentRequest articleCommentRequest) {
        // find article here first
        ArticleEntity articleEntity = getArticleEntity(slug);
        // create a new comment here
        CommentEntity commentEntity = new CommentEntity();
        commentEntity.setAuthor(userEntity);
        commentEntity.setBody(articleCommentRequest.getComment().getBody());
        commentEntity.setArticle_id(articleEntity.getId());
        return new ResponseEntity<>(ArticleCommentResponse.getFromCommentEntity(articleCommentService.addCommentEntity(commentEntity)), HttpStatus.CREATED);
    }

    @GetMapping("/articles/{slug}/comments")
    public ResponseEntity<ArticleCommentResponseWithCount> getArticleComments(@PathVariable(value = "slug") String slug,
                                                                              @RequestParam(value = "limit", required = false, defaultValue = "1") int limit,
                                                                              @RequestParam(value = "offset", required = false, defaultValue = "0") int offset) {
        ArticleEntity articleEntity = getArticleEntity(slug);
        // getByArticleId
        // Page<CommentEntity> page = articleCommentService.getComments(limit, offset, articleEntity.getId());
        return new ResponseEntity<>(ArticleCommentResponseWithCount.getFromCommentEntities(articleEntity.getComments().size(), articleEntity.getComments()), HttpStatus.OK);

    }

    public ArticleEntity getArticleEntity(String slug) {
        return articleService.getArticleBySlug(slug);
    }

    @ExceptionHandler({RuntimeException.class})
    ResponseEntity<ErrorEntity> handleExceptions(RuntimeException exception) {
        String message = exception.getMessage();
        HttpStatus errorStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        return new ResponseEntity<ErrorEntity>(
                ErrorEntity.from(message),
                errorStatus
        );
    }

}
