package com.scaler.conduit.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@AllArgsConstructor
@Data
@Builder
public class Profile {
    private final String image;
    private final String bio;
    private final String email;
    private final String username;
}
