package com.scaler.conduit.dtos.requests;

import lombok.Getter;

@Getter
public class UserUpdateRequest {
    private User user;

    @Getter
    public class User {
        private String username;
        private String email;
        private String password;
        private String bio;
        private String image;
    }
}
