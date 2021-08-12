package com.scaler.conduit.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Getter
@Setter
@NoArgsConstructor
@Entity(name = "favourites")
public class ArticleFavouriteEntity extends BaseEntity {

    ArticleEntity article;
    UserEntity user;

    @ManyToOne
    public ArticleEntity getArticle() {
        return article;
    }

    @ManyToOne
    public UserEntity getUser() {
        return user;
    }
}
