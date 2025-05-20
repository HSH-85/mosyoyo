package com.project.msy.user.service;

import com.project.msy.admin.dashboard.dto.UserDailySignupDto;
import com.project.msy.auth.dto.LoginRequest;
import com.project.msy.auth.dto.LoginResponse;
import com.project.msy.auth.dto.RegisterRequest;
import com.project.msy.user.dto.UserSummaryDto;
import com.project.msy.user.entity.Role;
import com.project.msy.user.entity.User;
import com.project.msy.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    /**
     * 전체 회원 요약 정보를 조회합니다.
     */
    public List<UserSummaryDto> findAllUsers() {
        return userRepository.findAll().stream()
                .map(UserSummaryDto::new)
                .collect(Collectors.toList());
    }

    /**
     * ID로 회원을 조회합니다.
     */
    public User findUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 회원을 찾을 수 없습니다. id=" + id));
    }

    /**
     * ID로 회원을 삭제합니다.
     */
    public void deleteUserById(Long id) {
        if (!userRepository.existsById(id)) {
            throw new IllegalArgumentException("삭제할 회원이 존재하지 않습니다. id=" + id);
        }

        try {
            userRepository.deleteById(id);
        } catch (DataIntegrityViolationException ex) {
            throw new RuntimeException("이 회원은 다른 데이터에 연결되어 있어 삭제할 수 없습니다.", ex);
        }
    }

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
                .name(request.getName())
                .phone(request.getPhone())
                .password(passwordEncoder.encode(request.getPassword()))
                .createdAt(LocalDateTime.now())
                .role(Role.USER)
                .build();

        userRepository.save(user);
    }

    public List<UserDailySignupDto> getDailyUserGrowth() {
        return userRepository.findDailySignupsForLast30Days();
    }
}
