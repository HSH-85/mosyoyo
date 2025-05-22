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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
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
        LocalDate today = LocalDate.now();
        LocalDate start = today.minusDays(29);  // 30일 전

        // 1) DB에서 raw 조회 (날짜 순서 보장)
        List<UserDailySignupDto> rawList = userRepository.findDailySignupsForLast30Days();

        // 2) (날짜 → count) 맵으로 변환
        Map<LocalDate, Long> countMap = rawList.stream()
                .collect(Collectors.toMap(
                        dto -> LocalDate.parse(dto.getDate()),   // "yyyy-MM-dd" 포맷
                        UserDailySignupDto::getCount
                ));

        // 3) 전체 합계(total)는 첫 DTO에서 꺼내거나, 없으면 0
        long total = rawList.isEmpty() ? 0L : rawList.get(0).getTotal();

        // 4) 지난 30일을 순회하며 없는 날은 0으로 채워 DTO 생성
        List<UserDailySignupDto> result = new ArrayList<>(30);
        for (int i = 0; i < 30; i++) {
            LocalDate date = start.plusDays(i);
            long cnt = countMap.getOrDefault(date, 0L);
            result.add(new UserDailySignupDto(date.toString(), cnt, total));
        }

        return result;
    }
}
