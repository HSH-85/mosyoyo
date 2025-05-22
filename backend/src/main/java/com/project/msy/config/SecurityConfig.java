package com.project.msy.config;

import com.project.msy.auth.oauth.CustomOAuth2UserService;
import com.project.msy.auth.oauth.OAuth2LoginSuccessHandler;
import com.project.msy.config.JwtFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtFilter jwtFilter;
    private final UserDetailsService customUserDetailsService;
    private final CustomOAuth2UserService customOAuth2UserService;
    private final OAuth2LoginSuccessHandler oauth2LoginSuccessHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // CORS 설정
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                // CSRF 비활성화 (JWT 사용 시)
                .csrf(csrf -> csrf.disable())
                // 세션 사용 안 함 (stateless)
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // 폼 로그인 및 HTTP Basic 비활성화
                .formLogin(fl -> fl.disable())
                .httpBasic(basic -> basic.disable())
                // 인증Providers 등록
                .authenticationProvider(authenticationProvider())
                // 경로별 권한 설정
                .authorizeHttpRequests(auth -> auth
//                        어드민 상품 create,read 임시 승인 설정 추후에 변경해야함
                                .requestMatchers("/api/facility/**").permitAll()
                                .requestMatchers("/api/facility").permitAll()

                                .requestMatchers(HttpMethod.POST, "/api/auth/**").permitAll()
                                .requestMatchers(HttpMethod.GET, "/api/auth/**").permitAll()
                                .requestMatchers(HttpMethod.GET, "/api/categories/**").permitAll()
                                .requestMatchers(HttpMethod.GET, "/api/origins/**").permitAll()
                                .requestMatchers(HttpMethod.GET, "/api/products/**").permitAll()
                                .requestMatchers(HttpMethod.GET, "/api/notices/**").permitAll()
                                .requestMatchers(HttpMethod.POST, "/api/notices/**").permitAll()
                                .requestMatchers(HttpMethod.PUT, "/api/notices/**").permitAll()
                                .requestMatchers(HttpMethod.DELETE, "/api/notices/**").permitAll()
                                .requestMatchers(HttpMethod.GET, "/api/reviews/**").permitAll()
                                .requestMatchers(HttpMethod.POST, "/api/reviews/**").permitAll()
                                .requestMatchers(HttpMethod.GET, "/api/videos/**").permitAll()
                                .requestMatchers("/api/email/**", "/api/auth/check-userid").permitAll()


                                // 상품 등록은 인증 필요
                                .requestMatchers(HttpMethod.POST, "/api/products/**").authenticated()
                                .requestMatchers(HttpMethod.PUT, "/api/products/**").authenticated()
                                .requestMatchers(HttpMethod.DELETE, "/api/products/**").authenticated()
                                // 나머지 API는 인증 필요
                                .requestMatchers("/api/**").authenticated()
                                // 어드민 페이지는 ADMIN 권한 필요
                                .requestMatchers("/admin/**").hasRole("ADMIN")
                                .requestMatchers("/user/**").hasAnyRole("USER", "ADMIN")
                                // 그 외 Static 리소스 및 SPA 라우팅은 모두 허용
                                .anyRequest().permitAll()
                )
                // OAuth2 로그인 설정
                .oauth2Login(oauth -> oauth
                        .userInfoEndpoint(ui -> ui.userService(customOAuth2UserService))
                        .successHandler(oauth2LoginSuccessHandler)
                )
                // JWT 필터를 UsernamePasswordAuthenticationFilter 앞에 등록
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOriginPatterns(List.of("http://localhost:3000"));
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        var provider = new org.springframework.security.authentication.dao.DaoAuthenticationProvider();
        provider.setUserDetailsService(customUserDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public org.springframework.security.authentication.AuthenticationManager authenticationManager(
            AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
