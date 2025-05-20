package com.project.msy.auth.oauth;

import com.project.msy.config.JwtUtil;
import com.project.msy.user.entity.Provider;
import com.project.msy.user.entity.Role;
import com.project.msy.user.entity.User;
import com.project.msy.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * OAuth2 로그인 성공 시 호출되는 핸들러
 * – Postman 등 “프론트 없는” 테스트 시에는 JSON 응답 사용 가능 (주석 처리된 부분)
 * – 실제 프론트 연동 시에는 redirect URL 로 토큰과 신규가입 여부 전달
 */
@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication)
            throws IOException, ServletException {

        // 1) 인증된 OAuth2User 정보 추출
        DefaultOAuth2User oauthUser = (DefaultOAuth2User) authentication.getPrincipal();
        OAuth2AuthenticationToken authToken = (OAuth2AuthenticationToken) authentication;
        String registrationId = authToken.getAuthorizedClientRegistrationId(); // "google" or "kakao"
        String providerId = oauthUser.getName();                              // 예: 구글 sub, 카카오 id
        String oauthId = registrationId.toUpperCase() + "_" + providerId;
        Provider provider = Provider.valueOf(registrationId.toUpperCase());

        // 2) DB에서 소셜 유저 조회 (Optional 처리)
        Optional<User> userOpt = userRepository.findByOauthIdAndProvider(oauthId, provider);
        boolean isNewUser = userOpt.isEmpty();
        User user = userOpt.orElse(null);

        // 3) JWT 생성용 subject와 role 결정
        //    - 내부 회원(userId)이 있으면 userId 사용
        //    - 소셜 신규/기존 모두 subject에는 oauthId 사용
        String subject;
        String roleName;
        if (user != null && user.getUserId() != null) {
            subject  = user.getUserId();
            roleName = user.getRole().name();
        } else {
            subject  = oauthId;
            roleName = Role.USER.name();
        }
        String token = jwtUtil.generateToken(subject, roleName);

        // =========================
        // ✅ 4-A: JSON 응답 (Postman 테스트용)
        // =========================

//        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
//        response.setContentType("application/json");
//        response.getWriter().write("{\"token\":\"" + token + "\"}");



        // =========================
        // ✅ 4-B: Redirect 응답 (프론트 연동용)
        //  - 프론트에서 /login/oauth2/code/{provider} 라우트로 토큰과 신규가입 여부를 전달
        // =========================

        String frontRedirectBase = "http://localhost:3000/login/oauth2/code/" + registrationId;
        UriComponentsBuilder uriBuilder = UriComponentsBuilder
                .fromUriString(frontRedirectBase)
                .queryParam("token", URLEncoder.encode(token, StandardCharsets.UTF_8));
        if (isNewUser) {
            uriBuilder.queryParam("isNewUser", "true");
        }
        String redirectUrl = uriBuilder.build().toUriString();

        response.sendRedirect(redirectUrl);
    }
}
