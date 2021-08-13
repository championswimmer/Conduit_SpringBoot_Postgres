package com.scaler.conduit.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
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


    private Set<ArticleEntity> favorited;

    @ManyToMany
    @JoinTable(name = "favourites")
    public Set<ArticleEntity> getFavorited() {
        return favorited;
    }

}
