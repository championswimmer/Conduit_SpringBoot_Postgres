package com.scaler.conduit.dtos;

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
