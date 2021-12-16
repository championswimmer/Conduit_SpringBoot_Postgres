package com.scaler.conduit.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class CommentResponse {
    Comment comment;
}
