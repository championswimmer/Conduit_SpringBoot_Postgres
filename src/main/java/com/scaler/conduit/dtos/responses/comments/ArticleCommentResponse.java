package com.scaler.conduit.dtos.responses.comments;

import com.scaler.conduit.entities.CommentEntity;
import com.scaler.conduit.entities.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.Date;

@Getter
@AllArgsConstructor
public class ArticleCommentResponse {

    Comment comment;

    @Builder
    @Getter
    static class Comment {
        long id;
        Date createdAt;
        Date updateAt;
        String body;
        User author;

        @Getter
        @Builder
        static class User {
            String username;
            String bio;
            String image;
            boolean following;
        }
    }

    public static ArticleCommentResponse getFromCommentEntity(CommentEntity commentEntity) {
        UserEntity userEntity = commentEntity.getAuthor();
        return new ArticleCommentResponse(Comment.builder().id(commentEntity.getId()).createdAt(commentEntity.getCreatedAt())
                .updateAt(commentEntity.getUpdatedAt()).body(commentEntity.getBody()).author(Comment.User.builder()
                        .bio(userEntity.getBio()).username(userEntity.getUsername()).image(userEntity.getImage())
                        .build()).build());
    }
}
