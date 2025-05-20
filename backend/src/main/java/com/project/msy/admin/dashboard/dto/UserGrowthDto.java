package com.project.msy.admin.dashboard.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
public class UserGrowthDto {
    private String month;  // 형식: "YYYY-MM"
    private Long  users;   // 신규 회원 수

    // Wrapper(Long) 용
    public UserGrowthDto(String month, Long users) {
        this.month = month;
        this.users = users;
    }

    // Primitive(long) 용
    public UserGrowthDto(String month, long users) {
        this.month = month;
        this.users = users;
    }
}