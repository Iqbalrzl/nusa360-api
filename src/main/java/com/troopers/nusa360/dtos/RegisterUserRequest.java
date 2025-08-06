package com.troopers.nusa360.dtos;

import lombok.Data;

@Data
public class RegisterUserRequest {
    private String username;
    private String email;
    private String password;
}
