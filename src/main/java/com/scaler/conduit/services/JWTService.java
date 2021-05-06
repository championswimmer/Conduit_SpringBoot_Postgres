package com.scaler.conduit.services;

import com.scaler.conduit.entities.UserEntity;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Service
public class JWTService {
    // TODO: this should be in .properties file, not checked into git
    public static final String JWT_KEY = "dL9nLwtKCjFfgj9gwuhJwqw4R4iepkfzC5XGdLfpyFTKuWDcpGCeZDBghzitHUjn";
    public static final int JWT_EXPIRY_AGE = 1000 * 60 * 60 * 24 * 7; // 1 week

    private final Key key = Keys.hmacShaKeyFor(JWT_KEY.getBytes(StandardCharsets.UTF_8));
    JwtParser jwtParser = Jwts.parserBuilder().setSigningKey(key).build();

    public String createJwt(UserEntity userEntity) {
        return Jwts.builder()
                .setSubject(userEntity.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + JWT_EXPIRY_AGE))
                .signWith(key)
                .compact();
    }

    public String decodeJwt(String jwts) {
        Claims claims = jwtParser.parseClaimsJws(jwts).getBody();
        return claims.getSubject();
    }
}
