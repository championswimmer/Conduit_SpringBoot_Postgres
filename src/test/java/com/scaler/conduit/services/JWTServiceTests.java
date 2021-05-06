package com.scaler.conduit.services;

import com.scaler.conduit.entities.UserEntity;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class JWTServiceTests {
    JWTService jwtService = new JWTService();

    @Test
    void createJWT_can_creat_JWT_from_userEntity() {
        UserEntity u = new UserEntity();
        u.setUsername("arnav");
        String jwt = jwtService.createJwt(u);
        assertNotNull(jwt);
        System.out.println(jwt);
    }

    @Test
    void decodeJWT_can_get_username_from_JWT() {
        String jwts = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhcm5hdiIsImlhdCI6MTYxOTg4NTkwMCwiZXhwIjoxODI5ODg1OTY3fQ.LPad1t4Mo3M26KaXTQ6XeD7XSBvfa4ifnyFRWHVRJs-uBOApwUeNNONeAn_tSgvm0YV3OYbZtu0sWN1r7PIgJg";
        String username =  jwtService.decodeJwt(jwts);
        assertEquals("arnav", username);
    }
}
