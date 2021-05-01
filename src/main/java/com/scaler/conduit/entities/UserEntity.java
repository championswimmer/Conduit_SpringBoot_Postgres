package com.scaler.conduit.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Transient;

@Entity(name = "users")
@Getter
@Setter
public class UserEntity extends BaseEntity {

    private String email;
    private String password;
    private String bio;
    private String image;
    private String username;
    private String token;

    @Transient
    public String getToken() {
        return token;
    }

    @JsonIgnore
    public String getPassword() {
        return password;
    }
}
