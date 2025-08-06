package com.troopers.nusa360.dtos;

import lombok.Data;

@Data
public class UpdateUserRequest {
    private String username;
    private String email;
}
