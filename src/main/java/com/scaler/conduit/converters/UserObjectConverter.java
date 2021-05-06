package com.scaler.conduit.converters;

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
}
