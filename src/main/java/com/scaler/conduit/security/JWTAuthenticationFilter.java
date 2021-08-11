package com.scaler.conduit.security;

import com.scaler.conduit.entities.UserEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.security.web.authentication.AuthenticationFilter;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;

@Component
public class JWTAuthenticationFilter extends AuthenticationFilter {
    public static class JWTAuthentication implements Authentication {
        private final String jwtString;
        private UserEntity userEntity;

        public void setUserEntity(UserEntity userEntity) {
            this.userEntity = userEntity;
        }

        public JWTAuthentication(String jwtString) {
            this.jwtString = jwtString;
        }

        @Override
        public Collection<? extends GrantedAuthority> getAuthorities() {
            return null;
        }

        @Override
        public String getCredentials() {
            return jwtString;
        }

        @Override
        public Object getDetails() {
            return null;
        }

        @Override
        public UserEntity getPrincipal() {
            return userEntity;
        }

        @Override
        public boolean isAuthenticated() {
            return userEntity != null;
        }

        @Override
        public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {

        }

        @Override
        public String getName() {
            return null;
        }
    }

    static class Converter implements AuthenticationConverter {
        Logger logger = LoggerFactory.getLogger(Converter.class);

        @Override
        public Authentication convert(HttpServletRequest request) {
            String authHeader = request.getHeader("Authorization");
            if (authHeader == null) {
                logger.info("Authorization Header Not Present " + request.getRequestURL());
                return null;
            }

            if (!authHeader.startsWith("Bearer ")) {
                logger.info("Authorization is not of Token type " + request.getRequestURL());
                return null;
            }

            String jwts = authHeader.replace("Bearer ", "");

            return new JWTAuthentication(jwts);
        }
    }

    public JWTAuthenticationFilter(JWTAuthManager jwtAuthManager) {
        super(jwtAuthManager, new Converter());
        this.setSuccessHandler((request, response, authentication) -> {
            SecurityContextHolder.getContext().setAuthentication(authentication);
        });
    }


}
