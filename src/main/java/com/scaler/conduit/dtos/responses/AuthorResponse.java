package com.scaler.conduit.dtos.responses;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class AuthorResponse {
    private String username;
    private String bio;
    private String image;
    private boolean following;
}
