package com.scaler.conduit.services;

import com.scaler.conduit.entities.CommentEntity;
import com.scaler.conduit.repositories.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class ArticleCommentService {

    @Autowired
    CommentRepository commentRepository;

    public CommentEntity addCommentEntity(CommentEntity commentEntity) {
        return commentRepository.save(commentEntity);
    }

    /*public Page<CommentEntity> getComments(int limit, int offset, long articleId) {
        return commentRepository.findCommentEntitiesByArticle_id(articleId, getPageRequest(limit, offset));
    }

    private PageRequest getPageRequest(int limit, int offset) {
        return PageRequest.of(offset, limit);
    }*/
}
