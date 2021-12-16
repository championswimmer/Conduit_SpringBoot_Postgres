package com.scaler.conduit.repositories;

import com.scaler.conduit.entities.ArticleEntity;
import com.scaler.conduit.entities.CommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<CommentEntity, Long> {

    List<CommentEntity> findCommentEntityByArticle(ArticleEntity articleEntity);
}
