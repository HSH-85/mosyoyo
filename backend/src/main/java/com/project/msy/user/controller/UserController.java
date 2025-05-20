package com.project.msy.user.controller;

import com.project.msy.auth.dto.RegisterRequest;
import com.project.msy.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // ✅ 아이디 중복 검사 API
    @GetMapping("/check-userid")
    public boolean checkUserId(@RequestParam String userId) {
        return userService.isUserIdDuplicate(userId);
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody RegisterRequest request) {
        userService.signup(request);
        return ResponseEntity.ok("회원가입 성공");
    }
}