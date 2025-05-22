package com.project.msy.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class EmailAuthService {

    private final StringRedisTemplate redisTemplate;
    private static final String RESET_PREFIX = "reset:";

    // 인증 코드 생성
    public String generateAndSendCode(String email) {
        String code = createRandomCode();
        redisTemplate.opsForValue().set(email, code, Duration.ofMinutes(5)); // 5분 TTL
        return code; // MailService에서 이메일로 이 코드 전송
    }

    // 인증 코드 검증
    public boolean verifyCode(String email, String inputCode) {
        String storedCode = redisTemplate.opsForValue().get(email);
        return storedCode != null && storedCode.equals(inputCode);
    }

    // ✅ 비밀번호 재설정용 코드 저장
    public String generateAndSendResetCode(String email) {
        String code = createRandomCode();
        redisTemplate.opsForValue().set(RESET_PREFIX + email, code, Duration.ofMinutes(5)); // reset용
        return code;
    }

    // ✅ 비밀번호 재설정용 코드 검증
    public boolean verifyResetCode(String email, String inputCode) {
        String storedCode = redisTemplate.opsForValue().get(RESET_PREFIX + email);
        return storedCode != null && storedCode.equals(inputCode);
    }

    // ✅ 사용한 인증코드 삭제
    public void deleteResetCode(String email) {
        redisTemplate.delete(RESET_PREFIX + email);
    }

    // 인증 코드 생성 로직 (6자리 숫자)
    private String createRandomCode() {
        Random random = new Random();
        int code = 100000 + random.nextInt(900000); // 100000 ~ 999999
        return String.valueOf(code);
    }
}
