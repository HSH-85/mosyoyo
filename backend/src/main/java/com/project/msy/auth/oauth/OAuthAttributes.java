package com.project.msy.auth.oauth;

import com.project.msy.user.entity.Provider;
import com.project.msy.user.entity.Role;
import com.project.msy.user.entity.User;
import lombok.Getter;

import java.util.Map;

@Getter
public class OAuthAttributes {

    private final Map<String, Object> attributes;
    private final String nameAttributeKey;
    private final String name;
    private final String email;
    private final String providerId; // ✅ 추가

    public OAuthAttributes(Map<String, Object> attributes, String nameAttributeKey, String name, String email, String providerId) {
        this.attributes = attributes;
        this.nameAttributeKey = nameAttributeKey;
        this.name = name;
        this.email = email;
        this.providerId = providerId;
    }

    public static OAuthAttributes of(String registrationId, String userNameAttributeName, Map<String, Object> attributes) {
        if ("kakao".equals(registrationId)) {
            return ofKakao(userNameAttributeName, attributes);
        }
        return ofGoogle(userNameAttributeName, attributes);
    }

    private static OAuthAttributes ofGoogle(String userNameAttributeName, Map<String, Object> attributes) {
        return new OAuthAttributes(
                attributes,
                userNameAttributeName,
                (String) attributes.get("name"),
                (String) attributes.get("email"),
                (String) attributes.get("sub") // ✅ Google의 고유 식별자
        );
    }

    @SuppressWarnings("unchecked")
    private static OAuthAttributes ofKakao(String userNameAttributeName, Map<String, Object> attributes) {
        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
        Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");

        return new OAuthAttributes(
                attributes,
                userNameAttributeName,
                (String) profile.get("nickname"),
                (String) kakaoAccount.get("email"),
                String.valueOf(attributes.get("id")) // ✅ Kakao의 고유 식별자
        );
    }

    public User toEntity(String providerName) {
        return User.builder()
                .name(name)
                .email(email)
                .oauthId(providerId)
                .role(Role.USER)
                .provider(Provider.valueOf(providerName.toUpperCase()))
                .build();
    }
}
