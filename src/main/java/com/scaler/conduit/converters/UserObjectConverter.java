package com.scaler.conduit.converters;

import com.scaler.conduit.dtos.responses.Profile;
import com.scaler.conduit.dtos.responses.UserProfileResponse;
import com.scaler.conduit.dtos.responses.UserResponse;
import com.scaler.conduit.entities.UserEntity;
import com.scaler.conduit.services.JWTService;
import org.springframework.stereotype.Service;

@Service
public class UserObjectConverter {
    private final JWTService jwtService;

    public UserObjectConverter(JWTService jwtService) {
        this.jwtService = jwtService;
    }

    public UserResponse entityToResponse(UserEntity userEntity) {
        return UserResponse.fromUserEntity(userEntity, jwtService.createJwt(userEntity));
    }

    public UserProfileResponse entityToUserProfileResponse(UserEntity userEntity) {
        return UserProfileResponse.builder()
                .profile(Profile.builder()
                        .bio(userEntity.getBio())
                        .username(userEntity.getUsername())
                        .image(userEntity.getImage())
                        .email(userEntity.getEmail())
                        .build())
                .build();
    }
}
