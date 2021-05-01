package com.scaler.conduit.services;

import com.scaler.conduit.entities.UserEntity;
import com.scaler.conduit.repositories.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepo;
    private final JWTService jwtService;
    private final BCryptPasswordEncoder bcryptEncoder;

    public static class UserNotFoundException extends RuntimeException {
        public UserNotFoundException() {
            super("No such user found");
        }
    }

    @Bean
    public static BCryptPasswordEncoder bcryptEncoder() {
        return new BCryptPasswordEncoder();
    }

    public UserService(UserRepository userRepo, JWTService jwtService, BCryptPasswordEncoder bcryptEncoder) {
        this.userRepo = userRepo;
        this.jwtService = jwtService;
        this.bcryptEncoder = bcryptEncoder;
    }

    public UserEntity registerNewUser(String username, String password, String email) {
        UserEntity newUser = new UserEntity();
        newUser.setUsername(username);
        newUser.setEmail(email);
        newUser.setPassword(bcryptEncoder.encode(password));
        newUser = userRepo.save(newUser);
        newUser.setToken(jwtService.createJwt(newUser));

        return userRepo.save(newUser);
    }

    /**
     * useful for login
     * @return
     */
    public UserEntity verifyUser(String email, String password) {
        return null;
    }

    public UserEntity findUserById(Long userId) {
        return null;
    }

    public UserEntity updateUserDetails(UserEntity userEntity) {
        return null;
    }


    public UserEntity findUserByUsername(String username) {
        UserEntity user = userRepo.findUserEntityByUsername(username);
        if (user == null) throw new UserNotFoundException();
        return user;
    }
}
