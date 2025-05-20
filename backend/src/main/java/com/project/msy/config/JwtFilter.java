package com.project.msy.config;

import com.project.msy.config.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;


@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        System.out.println("🔍 JwtFilter 실행됨");

        try {
            String authHeader = request.getHeader("Authorization");
            System.out.println("🔐 Authorization 헤더: " + authHeader);

            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                String token = authHeader.substring(7);
                System.out.println("📦 추출된 토큰: " + token);

                if (jwtUtil.validateToken(token)) {
                    System.out.println("🔑 토큰 유효성 검증 성공");

                    String userId = jwtUtil.extractUserId(token);
                    System.out.println("👤 추출된 userId: " + userId);

                    UserDetails userDetails = userDetailsService.loadUserByUsername(userId);
                    System.out.println("✅ UserDetails 조회 성공");

                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(
                                    userDetails, null, userDetails.getAuthorities());

                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    System.out.println("✅ 인증 성공: " + userId);
                } else {
                    System.out.println("❌ validateToken 실패");
                }
            } else {
                System.out.println("⚠️ Authorization 헤더 없음 또는 형식이 잘못됨");
            }

            filterChain.doFilter(request, response);

        } catch (Exception e) {
            e.printStackTrace(); // 로그로 확인
            throw new RuntimeException("JWT 필터 처리 중 오류 발생", e);
        }
    }

}
