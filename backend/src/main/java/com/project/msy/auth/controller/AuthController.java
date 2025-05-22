package com.project.msy.auth.controller;

import com.project.msy.auth.dto.*;
import com.project.msy.auth.security.CustomUserDetails;
import com.project.msy.auth.service.AuthService;
import com.project.msy.auth.service.EmailAuthService;
import com.project.msy.auth.service.MailService;
import com.project.msy.user.entity.User;
import com.project.msy.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final UserRepository userRepository;
    private final EmailAuthService emailAuthService;
    private final MailService mailService;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        authService.register(request);
        return ResponseEntity.ok("회원가입 성공");
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        LoginResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }
    @GetMapping("/me")
    public ResponseEntity<UserResponse> getCurrentUser(@AuthenticationPrincipal CustomUserDetails userDetails) {
        User user = userDetails.getUser();
        return ResponseEntity.ok(new UserResponse(user));
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> sendResetCode(@RequestBody EmailRequest request) {
        String email = request.getEmail();
        if (!userRepository.existsByEmail(email)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("success", false, "message", "등록되지 않은 이메일입니다."));
        }

        String code = emailAuthService.generateAndSendResetCode(email); // 코드 생성 + Redis 저장
        mailService.sendResetPasswordCode(email, code); // 메일 발송

        return ResponseEntity.ok(Map.of("success", true));
    }

    @PostMapping("/verify-reset-code")
    public ResponseEntity<?> verifyResetCode(@RequestBody EmailVerificationRequest request) {
        boolean valid = emailAuthService.verifyResetCode(request.getEmail(), request.getCode());
        return ResponseEntity.ok(Map.of("valid", valid));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordRequest request) {
        boolean verified = emailAuthService.verifyResetCode(request.getEmail(), request.getCode());
        if (!verified) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("success", false, "message", "인증 코드가 유효하지 않습니다."));
        }

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);

        emailAuthService.deleteResetCode(request.getEmail());

        return ResponseEntity.ok(Map.of("success", true));
    }
}
