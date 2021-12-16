package com.scaler.conduit.entities;

import lombok.*;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Entity(name = "articles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ArticleEntity extends BaseEntity {
    private String slug;
    private String title;
    private String description;
    private String body;
    private UserEntity author;
    private List<CommentEntity> comments;
    private List<TagEntity> tags;
    private Set<UserEntity> fans;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "favourites")
    public Set<UserEntity> getFans() {
        return fans;
    }

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
