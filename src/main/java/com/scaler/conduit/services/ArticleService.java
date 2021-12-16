package com.scaler.conduit.services;

import com.scaler.conduit.dtos.requests.ArticleUpdateRequest;
import com.scaler.conduit.dtos.requests.CommentCreateRequest;
import com.scaler.conduit.entities.ArticleEntity;
import com.scaler.conduit.entities.CommentEntity;
import com.scaler.conduit.entities.UserEntity;
import com.scaler.conduit.repositories.ArticleRepository;
import com.scaler.conduit.repositories.CommentRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;

import static java.util.Objects.isNull;

@Service
public class ArticleService {
    private final ArticleRepository articleRepo;
    private final CommentRepository commentRepo;
    private final TagsService tagsService;

    public static class AccessDeniedException extends RuntimeException {
        public AccessDeniedException() {
            super("Access Denied");
        }
    }

    public ArticleService(ArticleRepository articleRepo, CommentRepository commentRepo,
                          TagsService tagsService) {
        this.articleRepo = articleRepo;
        this.commentRepo = commentRepo;
        this.tagsService = tagsService;
    }

    public List<ArticleEntity> getAllArticles(int limit, int offset) {
        PageRequest pr = PageRequest.of((offset / limit), limit);
        return articleRepo.findAll(pr).getContent();
    }

    public ArticleEntity getArticleBySlug(String slug) {
        return articleRepo.findArticleEntityBySlug(slug);
    }

    public String getSlug(String title) {
        return title.toLowerCase(Locale.ROOT).replace(' ', '-');
    }

    public ArticleEntity createArticle(String articleTitle, String articleDescription, String articleBody, List<String> articleTagList, UserEntity currentLoggedInUser) {
        var articleTagEntities = tagsService.createTags(articleTagList);
        var articleSlug = this.getSlug(articleTitle);
        var articleEntity = ArticleEntity.builder()
                .slug(articleSlug)
                .title(articleTitle)
                .description(articleDescription)
                .body(articleBody)
                .tags(articleTagEntities)
                .author(currentLoggedInUser)
                .build();
        return articleRepo.save(articleEntity);
    }

    public List<ArticleEntity> getArticleFeed(UserEntity userEntity, int limit, int offset) {
        PageRequest pr = PageRequest.of((offset / limit), limit);
        return articleRepo.findArticleFeed(userEntity.getId(), pr);
    }

    public List<ArticleEntity> getAllArticleList(
            String tag, String author, String favorited, int limit, int offset
    ) {
        PageRequest pr = PageRequest.of((offset / limit), limit);
        return articleRepo.findByAuthorOrTagOrFavorited(tag, author, favorited, pr);
    }

    public ArticleEntity updateArticle(ArticleUpdateRequest.Article article, String slug, UserEntity user) {

        ArticleEntity oldArticle = articleRepo.findArticleEntityBySlug(slug);
        if (oldArticle.getAuthor().getId() != user.getId())
            throw new AccessDeniedException();
        if (!isNull(article.getBody())) oldArticle.setBody(article.getBody());
        if (!isNull(article.getDescription())) oldArticle.setBody(article.getDescription());
        if (!isNull(article.getTitle())) {
            oldArticle.setBody(article.getTitle());
            oldArticle.setSlug(getSlug(article.getTitle()));
        }
        return articleRepo.save(oldArticle);
    }

    public void deleteArticleBySlug(String slug, UserEntity userEntity) {
        ArticleEntity article = getArticleBySlug(slug);
        if (article.getAuthor().getId() != userEntity.getId())
            throw new AccessDeniedException();
        articleRepo.deleteBySlug(slug);
    }

    public CommentEntity addCommentToArticle(String slug, CommentCreateRequest.Comment body, UserEntity currentLoggedInUser) {
        ArticleEntity articleEntity = getArticleBySlug(slug);
        CommentEntity commentEntity = CommentEntity.builder()
                .body(body.getBody())
                .author(currentLoggedInUser)
                .article(articleEntity)
                .build();
        return commentRepo.save(commentEntity);
    }

    public List<CommentEntity> getAllCommentBySlug(String slug) {
        ArticleEntity articleEntity = getArticleBySlug(slug);
        return commentRepo.findCommentEntityByArticle(articleEntity);
    }

    public void deleteComment(long id, UserEntity userEntity) {
        CommentEntity comment = commentRepo.getOne(id);
        if (userEntity.getId() != comment.getAuthor().getId())
            throw new AccessDeniedException();
        commentRepo.deleteById(id);
    }

    public ArticleEntity favoriteArticle(String slug, UserEntity userEntity) {
        ArticleEntity articleEntity = getArticleBySlug(slug);
        articleEntity.getFans().add(userEntity);
        return articleRepo.saveAndFlush(articleEntity);
    }

    public ArticleEntity unfavoriteArticle(String slug, UserEntity userEntity) {
        ArticleEntity articleEntity = getArticleBySlug(slug);
        articleRepo.unfavoriteArticle(userEntity.getId(), articleEntity.getId());
        return articleEntity;
    }
}
