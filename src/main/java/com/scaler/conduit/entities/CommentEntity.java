package com.scaler.conduit.entities;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity(name = "comments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentEntity extends BaseEntity {
    private String body;

    private UserEntity author;
    private ArticleEntity article;

    @ManyToOne(targetEntity = UserEntity.class)
    public UserEntity getAuthor() {
        return author;
    }

    @ManyToOne(targetEntity = ArticleEntity.class)
    public ArticleEntity getArticle() {
        return article;
    }
}
