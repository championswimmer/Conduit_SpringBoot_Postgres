package com.scaler.conduit.controllers;

import com.scaler.conduit.dtos.UserSignupDto;
import com.scaler.conduit.entities.ErrorEntity;
import com.scaler.conduit.entities.UserEntity;
import com.scaler.conduit.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class ProfilesController {

    private final UserService users;

    public ProfilesController(UserService users) {
        this.users = users;
    }

    @GetMapping("/profiles/{username}")
    UserEntity getUserByUsername(@PathVariable("username") String username) {
        return users.findUserByUsername(username);
    }

    @PostMapping("/users")
    ResponseEntity<UserEntity> registerUser(@RequestBody UserSignupDto body) {
        UserEntity newUser =  users.registerNewUser(
                body.getUser().getUsername(),
                body.getUser().getPassword(),
                body.getUser().getEmail()
        );
        return new ResponseEntity<>(newUser, HttpStatus.CREATED);

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
