package com.scaler.conduit.services;

import com.scaler.conduit.dtos.requests.articles.CreateArticleRequest;
import com.scaler.conduit.entities.ArticleEntity;
import com.scaler.conduit.entities.TagEntity;
import com.scaler.conduit.entities.UserEntity;
import com.scaler.conduit.repositories.ArticleRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ArticleService {
    private final ArticleRepository articleRepo;

    private final TagsService tagsService;


    public static class ArticleNotFoundException extends RuntimeException {

        public ArticleNotFoundException() {
            super("Articles Not Found");
        }
    }

    public static class ArticleNotAuthorisedException extends RuntimeException {

        public ArticleNotAuthorisedException() {
            super("Article Not Authorised");
        }
    }

    public ArticleService(ArticleRepository articleRepo, TagsService tagsService) {
        this.articleRepo = articleRepo;
        this.tagsService = tagsService;
    }

    private Page<ArticleEntity> validateArticles(Page<ArticleEntity> page) {
        if (page == null || page.getContent().isEmpty()) {
            throw new ArticleNotFoundException();
        }
        return page;
    }

    public Page<ArticleEntity> getAllArticles(int limit, int offset) {
        return validateArticles(articleRepo.findAll(getPageRequest(limit, offset)));

    }


    // how to do pagination here
    public Page<ArticleEntity> getAllArticlesByUserName(String userName, int limit, int offset) {
        return validateArticles(articleRepo.findArticleEntitiesByAuthor_Username(userName, getPageRequest(limit, offset)));
    }

    public Page<ArticleEntity> getAllArticlesByTags(String tags,int limit, int offset) {
        List<TagEntity> tagEntities = new ArrayList<>();
        tagEntities.add(new TagEntity(tags));
        return validateArticles(articleRepo.findArticleEntitiesByTagsIn(tagEntities, getPageRequest(limit, offset)));
    }

    public ArticleEntity getArticleBySlug(String slug) {
        ArticleEntity articleEntity = articleRepo.findArticleEntityBySlug(slug);
        if (articleEntity == null) {
            throw new ArticleNotFoundException();
        }
        return articleEntity;
    }

    private PageRequest getPageRequest(int limit, int offset) {
        return PageRequest.of(offset, limit);
    }

    public ArticleEntity createArticle(CreateArticleRequest createArticleRequest, UserEntity userEntity) {
        ArticleEntity articleEntity = new ArticleEntity();
        articleEntity.setAuthor(userEntity);
        CreateArticleRequest.Article article = createArticleRequest.getArticle();
        articleEntity.setTitle(article.getTitle());
        articleEntity.setSlug(article.getTitle().replace(' ', '-'));
        articleEntity.setBody(article.getBody());
        articleEntity.setDescription(article.getDescription());
        if (article.getTagList() != null && article.getTagList().length > 0) {
            Set<TagEntity> tagEntities = new HashSet<>();
            for (String tags : article.getTagList()) {
                tagEntities.add(new TagEntity(tags));
            }
            articleEntity.setTags(new ArrayList<>(tagEntities));
            /**
             * Why we need this ???
             *
             * Reason for this to make articleTags as an persist state so that if cascasde.persit dont run for persit objects
             * else we will get duplicate key error for every tag inserted if already in the database.
             */
            articleEntity.setTags(getAllTags(articleEntity.getTags()));
        }
        return articleRepo.save(articleEntity);
    }

    private List<TagEntity> getAllTags(List<TagEntity> tagEntities) {
        return tagEntities.stream().map(tagEntity -> {
            return tagsService.saveTag(tagEntity.getName());
        }).collect(Collectors.toList());
    }

    public ArticleEntity updateArticleEntity(ArticleEntity articleEntity) {
        return articleRepo.save(articleEntity);
    }
}
