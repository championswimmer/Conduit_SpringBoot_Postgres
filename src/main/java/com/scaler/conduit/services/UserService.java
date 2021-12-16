package com.scaler.conduit.services;

import com.scaler.conduit.entities.UserEntity;
import com.scaler.conduit.repositories.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepo;
    private final BCryptPasswordEncoder bcryptEncoder;

    public static class UserNotFoundException extends RuntimeException {
        public UserNotFoundException() {
            super("No such user found");
        }
    }

    public static class UserPasswordIncorectException extends SecurityException {
        public UserPasswordIncorectException() {
            super("Invalid Password");
        }
    }

    public static class UsernameConflictException extends SecurityException {
        public UsernameConflictException() {
            super("Username already exists");
        }
    }

    @Bean
    public static BCryptPasswordEncoder bcryptEncoder() {
        return new BCryptPasswordEncoder();
    }

    public UserService(UserRepository userRepo, BCryptPasswordEncoder bcryptEncoder) {
        this.userRepo = userRepo;
        this.bcryptEncoder = bcryptEncoder;
    }

    public UserEntity registerNewUser(String username, String password, String email) {
        UserEntity user = userRepo.findUserEntityByUsername(username);
        if (user != null)
            throw new UsernameConflictException();

        UserEntity newUser = userRepo.save(UserEntity.builder()
                .username(username)
                .email(email)
                .password(bcryptEncoder.encode(password))
                .build());
        return userRepo.save(newUser);
    }

    /**
     * find and verify a user given email and password
     * userful for login
     *
     * @return user object if found and verified, else null
     */
    public UserEntity verifyUser(String email, String password) {
        UserEntity user = userRepo.findUserEntityByEmail(email);
        if (user == null)
            throw new UserNotFoundException();

        if (!bcryptEncoder.matches(password, user.getPassword()))
            throw new UserPasswordIncorectException();

        return user;
    }

    public UserEntity updateUser(UserEntity userEntity) {
        if (userEntity.getPassword() != null) {
            userEntity.setPassword(bcryptEncoder.encode(userEntity.getPassword()));
        }
        return userRepo.save(userEntity);
    }

    public UserEntity findUserByUsername(String username) {
        UserEntity user = userRepo.findUserEntityByUsername(username);
        if (user == null) throw new UserNotFoundException();
        return user;
    }

    public UserEntity followUser(String username, Long loggedInUserUserId) {
        UserEntity userToFollow = findUserByUsername(username);
        userRepo.followUser(loggedInUserUserId, userToFollow.getId());
        return userToFollow;
    }

    public UserEntity unfollowUser(String username, Long loggedInUserUserId) {
        UserEntity userToUnfollow = findUserByUsername(username);
        userRepo.unfollowUser(loggedInUserUserId, userToUnfollow.getId());
        return userToUnfollow;
    }

}
