package com.project.msy.user.dto;

import com.project.msy.user.entity.User;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class UserSummaryDto {
    private Long id;
    private String userId;
    private String name;
    private String email;
    private String phone;
    private LocalDateTime createdAt;
    private String role;

    public UserSummaryDto(User user) {
        this.id = user.getId();
        this.userId = user.getUserId();
        this.name = user.getName();
        this.email = user.getEmail();
        this.phone = user.getPhone();
        this.createdAt = user.getCreatedAt();
        this.role = user.getRole().name();
    }
}
