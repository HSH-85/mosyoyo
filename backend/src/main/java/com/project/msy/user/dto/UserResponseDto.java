package com.project.msy.user.dto;

import com.project.msy.user.entity.User;
import lombok.Builder;
import lombok.Getter;

@Getter
public class UserResponseDto {
    private Long id;
    private String userId;
    private String name;
    private String email;
    private String phone;
    private String oauthId;
    private String joinDate;

    @Builder
    public UserResponseDto(Long id, String userId, String name, String email, String phone, String joinDate, String oauthId) {
        this.id = id;
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.oauthId = oauthId;
        this.joinDate = joinDate;
    }

    public static UserResponseDto from(User user) {
        return UserResponseDto.builder()
                .id(user.getId())
                .userId(user.getUserId())
                .name(user.getName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .oauthId(user.getOauthId())
                .joinDate(user.getCreatedAt() != null ? user.getCreatedAt().toString() : "")
                .build();
    }
}
