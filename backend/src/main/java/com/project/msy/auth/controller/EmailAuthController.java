package com.project.msy.auth.controller;

import com.project.msy.auth.dto.EmailRequest;
import com.project.msy.auth.dto.EmailVerificationRequest;
import com.project.msy.auth.service.EmailAuthService;
import com.project.msy.auth.service.MailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/email")
@RequiredArgsConstructor
public class EmailAuthController {

    private final EmailAuthService emailAuthService;
    private final MailService mailService;

    /**
     * 인증코드 전송
     */
    @PostMapping("/send")
    public ResponseEntity<String> sendCode(@RequestBody EmailRequest request) {
        String code = emailAuthService.generateAndSendCode(request.getEmail());
        mailService.sendVerificationEmail(request.getEmail(), code);
        return ResponseEntity.ok("인증 코드가 전송되었습니다.");
    }

    /**
     * 인증코드 검증
     */
    @PostMapping("/verify")
    public ResponseEntity<String> verifyCode(@RequestBody EmailVerificationRequest request) {
        boolean success = emailAuthService.verifyCode(request.getEmail(), request.getCode());
        if (success) {
            return ResponseEntity.ok("인증 성공");
        } else {
            return ResponseEntity.badRequest().body("인증 실패: 코드가 일치하지 않거나 만료되었습니다.");
        }
    }
}
