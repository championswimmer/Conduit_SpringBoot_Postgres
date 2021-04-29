package com.scaler.conduit.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity(name = "comments")
@Getter
@Setter
public class CommentEntity extends BaseEntity {
    private String body;

    private UserEntity author;

    @ManyToOne(targetEntity = UserEntity.class)
    public UserEntity getAuthor() {
        return author;
    }
}
