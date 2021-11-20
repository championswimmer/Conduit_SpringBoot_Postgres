package com.scaler.conduit.dtos.requests;

import lombok.Getter;

@Getter
public class CommentCreateRequest {

    Comment comment;

    @Getter
    public class Comment {
        String body;
    }
}
