package com.scaler.conduit.services;

import com.scaler.conduit.entities.ArticleEntity;
import com.scaler.conduit.entities.UserEntity;
import com.scaler.conduit.repositories.ArticleRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;

@Service
public class ArticleService {
    private final ArticleRepository articleRepo;
    private final TagsService tagsService;

    public ArticleService(ArticleRepository articleRepo, TagsService tagsService) {
        this.articleRepo = articleRepo;
        this.tagsService = tagsService;
    }

    public List<ArticleEntity> getAllArticles(int limit, int offset) {
        PageRequest pr = PageRequest.of((offset / limit) , limit);
        return articleRepo.findAll(pr).getContent();
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
}
