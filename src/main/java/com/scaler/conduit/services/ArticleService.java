package com.scaler.conduit.services;

import com.scaler.conduit.entities.ArticleEntity;
import com.scaler.conduit.repositories.ArticleRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArticleService {
    private final ArticleRepository articleRepo;

    public ArticleService(ArticleRepository articleRepo) {
        this.articleRepo = articleRepo;
    }

    public List<ArticleEntity> getAllArticles(int limit, int offset) {
        PageRequest pr = PageRequest.of((offset / limit) + 1, limit);
        return articleRepo.findAll(pr).getContent();
    }
}
