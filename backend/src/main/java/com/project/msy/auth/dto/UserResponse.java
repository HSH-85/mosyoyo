package com.project.msy.auth.dto;

import com.project.msy.user.entity.User;
import lombok.Getter;

@Getter
public class UserResponse {

    private Long id;           // DB상의 고유 ID
    private String userId;     // 로그인 ID 또는 OAuth ID
    private String name;       // 사용자 이름
    private String email;      // 이메일 (null 가능)
    private String role;       // USER, ADMIN 등

    public UserResponse(User user) {
        this.id = user.getId();
        this.userId = user.getUserId();
        this.name = user.getName();
        this.email = user.getEmail();
        this.role = user.getRole().name(); // enum -> String
    }
}
