package com.scaler.conduit.services;

import com.scaler.conduit.entities.UserEntity;
import com.scaler.conduit.repositories.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepo;

    public static class UserNotFoundException extends RuntimeException {
        public UserNotFoundException() {
            super("No such user found");
        }
    }

    public UserService(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    public UserEntity registerNewUser(String username, String password, String email) {
        return null;
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
