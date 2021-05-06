package com.scaler.conduit.dtos.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class UserLoginRequest {

    @JsonProperty("user")
    private User user;

    @Getter
    public class User {

        @JsonProperty("password")
        private String password;

        @JsonProperty("email")
        private String email;
    }
}