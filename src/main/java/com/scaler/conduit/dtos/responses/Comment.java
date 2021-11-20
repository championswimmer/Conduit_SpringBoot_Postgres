package com.scaler.conduit.dtos.responses;

import lombok.Builder;
import lombok.Getter;

import java.util.Date;

@Builder
@Getter
public class Comment {

    private long id;
    private Date createdAt;
    private Date updatedAt;
    private String body;
    private AuthorResponse author;
}
