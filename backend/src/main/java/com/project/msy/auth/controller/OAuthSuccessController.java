package com.project.msy.auth.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OAuthSuccessController {

    @GetMapping("/login/success")
    public String loginSuccess() {
        return "소셜 로그인 성공! 🎉";
    }
}
