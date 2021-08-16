package com.scaler.conduit.dtos.requests;
import lombok.Getter;

@Getter
public class ArticleCommentRequest {

    Comment comment;

    @Getter
    public class Comment {
        public String body;
    }
}
