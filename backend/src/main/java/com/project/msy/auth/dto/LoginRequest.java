package com.project.msy.auth.dto;

import lombok.Getter;

@Getter
public class LoginRequest {
    private String userId;
    private String password;
}
