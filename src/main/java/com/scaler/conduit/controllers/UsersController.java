package com.scaler.conduit.controllers;

import com.scaler.conduit.dtos.UserLoginRequest;
import com.scaler.conduit.dtos.UserSignupRequest;
import com.scaler.conduit.dtos.UserUpdateRequest;
import com.scaler.conduit.entities.ErrorEntity;
import com.scaler.conduit.entities.UserEntity;
import com.scaler.conduit.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
public class UsersController {

    private final UserService users;

    public UsersController(UserService users) {
        this.users = users;
    }

    @GetMapping("/profiles/{username}")
    ResponseEntity<UserEntity> getUserByUsername(@PathVariable("username") String username) {
        return ResponseEntity.ok(users.findUserByUsername(username));
    }

    @GetMapping("/user")
    ResponseEntity<UserEntity> getCurrentUser(@AuthenticationPrincipal UserEntity userEntity) {
        return ResponseEntity.ok(userEntity);
    }

    @PostMapping("/users")
    ResponseEntity<UserEntity> registerUser(@RequestBody UserSignupRequest body) {
        UserEntity newUser = users.registerNewUser(
                body.getUser().getUsername(),
                body.getUser().getPassword(),
                body.getUser().getEmail()
        );
        return new ResponseEntity<>(newUser, HttpStatus.CREATED);

    }

    @PostMapping("/users/login")
    ResponseEntity<UserEntity> loginUser(@RequestBody UserLoginRequest body) {
        UserEntity user = users.verifyUser(
                body.getUser().getEmail(),
                body.getUser().getPassword()
        );
        return ResponseEntity.ok(user);
    }

    @PatchMapping("/user")
    ResponseEntity<UserEntity> updateUser(
            @AuthenticationPrincipal UserEntity userEntity,
            @RequestBody UserUpdateRequest body
    ) {
        if (body.getUser().getUsername() != null) userEntity.setUsername(body.getUser().getUsername());
        if (body.getUser().getBio() != null) userEntity.setBio(body.getUser().getBio());
        if (body.getUser().getImage() != null) userEntity.setImage(body.getUser().getImage());
        if (body.getUser().getEmail() != null) userEntity.setEmail(body.getUser().getEmail());
        userEntity.setPassword(body.getUser().getPassword());

        return new ResponseEntity<>(
                users.updateUser(userEntity),
                HttpStatus.ACCEPTED
        );

    }


    @ExceptionHandler({RuntimeException.class})
    ResponseEntity<ErrorEntity> handleExceptions(RuntimeException exception) {
        String message = exception.getMessage();
        HttpStatus errorStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        if (exception instanceof UserService.UserNotFoundException) {
            errorStatus = HttpStatus.NOT_FOUND;
        }

        return new ResponseEntity<ErrorEntity>(
                ErrorEntity.from(message),
                errorStatus
        );
    }

}
