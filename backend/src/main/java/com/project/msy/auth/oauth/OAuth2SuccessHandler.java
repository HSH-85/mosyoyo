package com.project.msy.auth.oauth;

import com.project.msy.config.JwtUtil;
import com.project.msy.user.entity.User;
import com.project.msy.user.repository.UserRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        DefaultOAuth2User oAuth2User = (DefaultOAuth2User) authentication.getPrincipal();
        String email = (String) oAuth2User.getAttribute("email");

        // 사용자 조회
        User user = (User) userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("OAuth 로그인 사용자 조회 실패"));

        // JWT 토큰 생성
        String token = jwtUtil.generateToken(user.getUserId() != null ? user.getUserId() : user.getEmail(), user.getRole().name());
        System.out.println("토큰 생성 : "  + token);

        // 리다이렉트 (예: 프론트로 토큰 전달)
        String redirectUrl = "http://localhost:3000/oauth2/success?token=" + URLEncoder.encode(token, StandardCharsets.UTF_8);
        getRedirectStrategy().sendRedirect(request, response, redirectUrl);
    }
}
