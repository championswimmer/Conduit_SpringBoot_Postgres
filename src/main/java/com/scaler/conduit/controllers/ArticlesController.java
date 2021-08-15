package com.scaler.conduit.controllers;

import com.scaler.conduit.dtos.requests.articles.CreateArticleRequest;
import com.scaler.conduit.dtos.responses.articles.ArticleResponse;
import com.scaler.conduit.dtos.responses.articles.ArticleResponseWithCount;
import com.scaler.conduit.entities.ArticleEntity;
import com.scaler.conduit.entities.ErrorEntity;
import com.scaler.conduit.entities.UserEntity;
import com.scaler.conduit.services.ArticleService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class ArticlesController {
    private final ArticleService articles;

    public ArticlesController(ArticleService articles) {
        this.articles = articles;
    }

    /**
     * Any user can see the articles
     *
     * @param limit
     * @param offset
     * @return
     */
    @GetMapping("/articles")
    ResponseEntity<ArticleResponseWithCount> getArticles(
            @RequestParam(value = "limit", required = false, defaultValue = "5") int limit,
            @RequestParam(value = "offset", required = false, defaultValue = "0") int offset
    ) {
        Page<ArticleEntity> page = articles.getAllArticles(limit, offset);
        return getReturnData(page);
    }

    private ResponseEntity<ArticleResponseWithCount> getReturnData(Page<ArticleEntity> page) {
        return ResponseEntity.ok(ArticleResponseWithCount.fromArticleEntity(page.getContent(), page.getTotalElements()));
    }

    @RequestMapping(value = "/articles", method = RequestMethod.GET, params = "author")
    ResponseEntity<ArticleResponseWithCount> getArticlesByUsername(@RequestParam(value = "author") String username,
                                                                   @RequestParam(value = "limit", required = false, defaultValue = "20") int limit,
                                                                   @RequestParam(value = "offset", required = false, defaultValue = "0") int offset
    ) {
        Page<ArticleEntity> page = articles.getAllArticlesByUserName(username, limit, offset);
        return getReturnData(page);

    }

    @RequestMapping(value = "/articles", method = RequestMethod.GET, params = "tag")
    ResponseEntity<ArticleResponseWithCount> getArticlesByTags(@RequestParam(value = "tag") String tag,
                                                               @RequestParam(value = "limit", required = false, defaultValue = "5") int limit,
                                                               @RequestParam(value = "offset", required = false, defaultValue = "0") int offset
    ) {
        Page<ArticleEntity> page = articles.getAllArticlesByTags(tag, limit, offset);
        return getReturnData(page);

    }


    /**
     * @param createArticleRequest
     * @return
     */
    @PostMapping("/articles")
    ResponseEntity<ArticleResponse> createArticle(@RequestBody CreateArticleRequest createArticleRequest, @AuthenticationPrincipal UserEntity userEntity) {
        return new ResponseEntity<>(ArticleResponse.fromArticleEntity(articles.createArticle(createArticleRequest, userEntity)), HttpStatus.CREATED);
    }


    @PutMapping("/articles/{slug}")
    public ResponseEntity<ArticleResponse> updateArticle(@PathVariable(value = "slug") String slug,
                                                         @RequestBody CreateArticleRequest createArticleRequest,
                                                         @AuthenticationPrincipal UserEntity userEntity) {

        ArticleEntity articleEntity = articles.getArticleBySlug(slug);
        if (articleEntity.getAuthor().getId() != userEntity.getId()) {
            throw new ArticleService.ArticleNotAuthorisedException();
        }
        CreateArticleRequest.Article article = createArticleRequest.getArticle();
        if (article.getBody() != null) {
            articleEntity.setBody(article.getBody());
        }
        if (article.getTitle() != null && !article.getTitle().isEmpty()) {
            articleEntity.setTitle(article.getTitle());
            articleEntity.setSlug(articleEntity.getTitle().replace(' ', '-'));
        }

        return new ResponseEntity<>(ArticleResponse.fromArticleEntity(articles.updateArticleEntity(articleEntity)), HttpStatus.OK);

    }

    @ExceptionHandler({RuntimeException.class})
    ResponseEntity<ErrorEntity> handleExceptions(RuntimeException exception) {
        String message = exception.getMessage();
        HttpStatus errorStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        if (exception instanceof ArticleService.ArticleNotFoundException) {
            errorStatus = HttpStatus.NOT_FOUND;
        }
        if (exception instanceof ArticleService.ArticleNotAuthorisedException) {
            errorStatus = HttpStatus.UNAUTHORIZED;
        }
        return new ResponseEntity<ErrorEntity>(
                ErrorEntity.from(message),
                errorStatus
        );
    }


}
