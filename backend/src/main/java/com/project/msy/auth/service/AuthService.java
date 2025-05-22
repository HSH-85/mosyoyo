package com.project.msy.auth.service;

import com.project.msy.auth.dto.LoginRequest;
import com.project.msy.auth.dto.LoginResponse;
import com.project.msy.auth.dto.RegisterRequest;
import com.project.msy.config.JwtUtil;
import com.project.msy.user.entity.Role;
import com.project.msy.user.entity.User;
import com.project.msy.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public void register(RegisterRequest request) {
        Optional<User> existing = userRepository.findByUserId(request.getUserId());
        if (existing.isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 사용자 ID입니다.");
        }

        User user = new User();
        user.setUserId(request.getUserId());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setCreatedAt(LocalDateTime.now());
        user.setOauthId("LOCAL_" + request.getUserId());
        user.setRole(Role.USER);

        userRepository.save(user);
    }

    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByUserId(request.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        String token = jwtUtil.generateToken(user.getUserId(), user.getRole().name());
        boolean isAdmin = user.getRole() == Role.ADMIN;  // Role이 enum이라면 ADMIN 값 확인
        Long userId = user.getId();

        return new LoginResponse(token, isAdmin,userId);
    }
}
