package com.scaler.conduit.dtos.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.scaler.conduit.entities.UserEntity;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class UserResponse {

    @JsonProperty("user")
    private final User user;

    @Getter
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class User {

        @JsonProperty("image")
        private final String image;

        @JsonProperty("bio")
        private final String bio;

        @JsonProperty("email")
        private final String email;

        @JsonProperty("token")
        private final String token;

        @JsonProperty("username")
        private final String username;
    }


    public static UserResponse fromUserEntity(UserEntity userEntity, String token) {
        return new UserResponse(
                new User(
                        userEntity.getImage(),
                        userEntity.getBio(),
                        userEntity.getEmail(),
                        token,
                        userEntity.getUsername()

                )
        );
    }

}