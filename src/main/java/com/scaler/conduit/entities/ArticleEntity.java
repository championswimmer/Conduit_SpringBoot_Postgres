package com.scaler.conduit.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity(name = "articles")
@Getter
@Setter
public class ArticleEntity extends BaseEntity {
    private String slug;
    private String title;
    private String description;
    private String body;
    private UserEntity author;
    private List<CommentEntity> comments;
    private List<TagEntity> tags;

    @ManyToOne(fetch = FetchType.EAGER)
    public UserEntity getAuthor() {
        return author;
    }

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "article_id")
    public List<CommentEntity> getComments() {
        return comments;
    }

    @ManyToMany(fetch = FetchType.EAGER)
    public List<TagEntity> getTags() {
        return tags;
    }
}
