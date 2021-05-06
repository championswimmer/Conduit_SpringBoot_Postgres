package com.scaler.conduit.controllers;

import com.scaler.conduit.converters.UserObjectConverter;
import com.scaler.conduit.dtos.requests.UserLoginRequest;
import com.scaler.conduit.dtos.requests.UserSignupRequest;
import com.scaler.conduit.dtos.requests.UserUpdateRequest;
import com.scaler.conduit.dtos.responses.UserResponse;
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
    private final UserObjectConverter converter;

    public UsersController(UserService users, UserObjectConverter converter) {
        this.users = users;
        this.converter = converter;
    }

    @GetMapping("/profiles/{username}")
    ResponseEntity<UserResponse> getUserByUsername(@PathVariable("username") String username) {
        return ResponseEntity.ok(
                converter.entityToResponse(users.findUserByUsername(username))
        );
    }

    @GetMapping("/user")
    ResponseEntity<UserResponse> getCurrentUser(@AuthenticationPrincipal UserEntity userEntity) {
        return ResponseEntity.ok(converter.entityToResponse(userEntity));
    }

    @PostMapping("/users")
    ResponseEntity<UserResponse> registerUser(@RequestBody UserSignupRequest body) {
        UserEntity newUser = users.registerNewUser(
                body.getUser().getUsername(),
                body.getUser().getPassword(),
                body.getUser().getEmail()
        );
        return new ResponseEntity<>(converter.entityToResponse(newUser), HttpStatus.CREATED);

    }

    @PostMapping("/users/login")
    ResponseEntity<UserResponse> loginUser(@RequestBody UserLoginRequest body) {
        UserEntity user = users.verifyUser(
                body.getUser().getEmail(),
                body.getUser().getPassword()
        );
        return ResponseEntity.ok(converter.entityToResponse(user));
    }

    @PatchMapping("/user")
    ResponseEntity<UserResponse> updateUser(
            @AuthenticationPrincipal UserEntity userEntity,
            @RequestBody UserUpdateRequest body
    ) {
        if (body.getUser().getUsername() != null) userEntity.setUsername(body.getUser().getUsername());
        if (body.getUser().getBio() != null) userEntity.setBio(body.getUser().getBio());
        if (body.getUser().getImage() != null) userEntity.setImage(body.getUser().getImage());
        if (body.getUser().getEmail() != null) userEntity.setEmail(body.getUser().getEmail());
        userEntity.setPassword(body.getUser().getPassword());

        return new ResponseEntity<>(
                converter.entityToResponse(users.updateUser(userEntity)),
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
