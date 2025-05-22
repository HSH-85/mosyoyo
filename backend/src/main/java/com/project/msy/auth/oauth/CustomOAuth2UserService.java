package com.project.msy.auth.oauth;

import com.project.msy.user.entity.Provider;
import com.project.msy.user.entity.Role;
import com.project.msy.user.entity.User;
import com.project.msy.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        // 1) OAuth2 서버에서 유저 정보 로드
        OAuth2User oauth2User = super.loadUser(userRequest);

        // 2) registrationId 및 userNameAttributeKey 추출
        String registrationId = userRequest.getClientRegistration().getRegistrationId(); // "google" or "kakao"
        String userNameAttributeName = userRequest.getClientRegistration()
                .getProviderDetails()
                .getUserInfoEndpoint()
                .getUserNameAttributeName();

        // 3) OAuthAttributes 매핑
        Map<String, Object> originalAttributes = oauth2User.getAttributes();
        OAuthAttributes attrs = OAuthAttributes.of(
                registrationId,
                userNameAttributeName,
                originalAttributes
        );

        // 4) DB 저장 또는 업데이트 로직
        User user = saveOrUpdate(attrs, registrationId);

        // 5) DefaultOAuth2User에 전달할 attribute 구성
        Map<String, Object> mappedAttrs = new HashMap<>(attrs.getAttributes());
        mappedAttrs.put("oauthId", user.getOauthId());
        Provider providerEnum = Provider.valueOf(registrationId.toUpperCase());
        mappedAttrs.put("provider", providerEnum.name());

        // 6) 권한 설정 (ROLE_ 접두사 포함)
        String authority = "ROLE_" + user.getRole().name();

        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority(authority)),
                mappedAttrs,
                "oauthId"
        );
    }

    /**
     * Attributes를 기반으로 DB에 저장 또는 업데이트
     */
    private User saveOrUpdate(OAuthAttributes attributes, String providerName) {
        String providerId = attributes.getProviderId();
        String oauthId = providerName.toUpperCase() + "_" + providerId;
        Provider providerEnum = Provider.valueOf(providerName.toUpperCase());

        // 1) oauthId + provider 로 먼저 조회
        Optional<User> userOpt = userRepository.findByOauthIdAndProvider(oauthId, providerEnum);

        // 2) 이메일이 제공된 경우, 기존 이메일 계정 조회
        if (userOpt.isEmpty() && attributes.getEmail() != null) {
            userOpt = userRepository.findByEmail(attributes.getEmail());
            if (userOpt.isPresent()) {
                // 기존 이메일 기반 회원에게 oauthId, provider 설정 후 업데이트
                User existing = userOpt.get();
                existing.setOauthId(oauthId);
                existing.setProvider(providerEnum);
                existing.setCreatedAt(LocalDateTime.now());
                return userRepository.save(existing);
            }
        }

        // 3) 기존 oauthId 기반 회원이 있으면 반환
        if (userOpt.isPresent()) {
            return userOpt.get();
        }

        // 4) 신규 회원 생성
        User newUser = User.builder()
                .oauthId(oauthId)
                .name(attributes.getName())
                .email(attributes.getEmail())
                .provider(providerEnum)
                .role(Role.USER)
                .createdAt(LocalDateTime.now())
                .build();
        return userRepository.save(newUser);
    }
}
