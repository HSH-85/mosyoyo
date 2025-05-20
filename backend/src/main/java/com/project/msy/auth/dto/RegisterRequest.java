package com.project.msy.auth.dto;

import lombok.Getter;
// 회원가입
@Getter
public class RegisterRequest {
    private String userId;
    private String password;
    private String name;
    private String email;
    private String phone;
}
