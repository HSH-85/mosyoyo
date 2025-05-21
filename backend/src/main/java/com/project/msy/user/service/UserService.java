package com.project.msy.user.service;

import com.project.msy.auth.dto.LoginRequest;
import com.project.msy.auth.dto.LoginResponse;
import com.project.msy.auth.dto.RegisterRequest;
import com.project.msy.user.entity.Role;
import com.project.msy.user.entity.User;
import com.project.msy.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder; // 비밀번호 암호화

    // ✅ userId 중복 검사
    public boolean isUserIdDuplicate(String userId) {
        return userRepository.existsByUserId(userId);
    }

    @Transactional
    public void signup(RegisterRequest request) {
        if (userRepository.existsByUserId(request.getUserId())) {
            throw new IllegalArgumentException("이미 존재하는 아이디입니다.");
        }

        User user = User.builder()
                .userId(request.getUserId())
                .email(request.getEmail())
                .phone(request.getPhone())
                .password(passwordEncoder.encode(request.getPassword())) // 비밀번호 암호화
                .role(Role.USER)
                .build();

        userRepository.save(user);
    }
}
