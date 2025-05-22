package com.project.msy.auth.security;

import com.project.msy.user.entity.User;
import com.project.msy.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service("customUserDetailsFromSecurity")
@Primary
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {

        String lookup = userId;
        if (userId.startsWith("KAKAO_KAKAO_")) {
            // 첫 번째 "KAKAO_" 제거
            lookup = userId.substring("KAKAO_".length());
        }

        if (userId.startsWith("GOOGLE_GOOGLE_")) {
            // 첫 번째 "GOOGLE_" 제거
            lookup = userId.substring("GOOGLE_".length());
        }

        // 1) userId 로 먼저 찾아보고
        Optional<User> userOpt = userRepository.findByUserId(lookup);
        // 2) 못 찾으면 oauthId 로 다시 시도
        if (userOpt.isEmpty()) {
            userOpt = userRepository.findByOauthId(lookup);
        }
        User user = userOpt
                .orElseThrow(() -> new UsernameNotFoundException("해당 사용자를 찾을 수 없습니다: " + userId));
        return new CustomUserDetails(user);
    }
}
