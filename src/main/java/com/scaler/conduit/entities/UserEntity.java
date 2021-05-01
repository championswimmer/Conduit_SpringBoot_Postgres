package com.scaler.conduit.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Transient;

@Entity(name = "users")
@Getter
@Setter
public class UserEntity extends BaseEntity {

    private String email;
    private String bio;
    private String image;
    private String username;
    @Transient
    private String token;

}
