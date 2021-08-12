package com.scaler.conduit.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import java.util.List;
import java.util.Set;

@Entity(name = "users")
@Getter
@Setter
public class UserEntity extends BaseEntity {

    private String email;
    private String password;
    private String bio;
    private String image;
    private String username;

//    @ManyToMany
//    @JoinTable(
//            name = "favourited_article",
//            joinColumns = @JoinColumn(name = "user_id"),
//            inverseJoinColumns = @JoinColumn(name = "article_id"))
//    private Set<ArticleEntity> favorited;
}
