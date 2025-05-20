package com.project.msy.config;

import com.project.msy.user.entity.Role;
import com.project.msy.user.entity.User;
import com.project.msy.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class AdminInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        String defaultAdminId = "admin";
        String defaultAdminEmail = "admin@example.com";

        // 이미 admin 계정이 존재하면 아무 작업도 하지 않음
        if (userRepository.findByUserId(defaultAdminId).isPresent()) {
            System.out.println("🔔 관리자 계정이 이미 존재합니다.");
            return;
        }

        // admin 계정 생성
        User admin = new User();
        admin.setUserId(defaultAdminId);
        admin.setName("관리자");
        admin.setPassword(passwordEncoder.encode("admin123")); // 초기 비밀번호는 반드시 나중에 변경해야 함
        admin.setEmail(defaultAdminEmail);
        admin.setRole(Role.ADMIN);
        admin.setOauthId("LOCAL_admin");
        admin.setCreatedAt(LocalDateTime.now());


        userRepository.save(admin);
        System.out.println("✅ 관리자 계정(admin) 생성 완료");
    }
}
