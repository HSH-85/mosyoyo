package com.project.msy.admin.dashboard.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
public class UserDailySignupDto {
    private String date;
    private Long count;
    private Long total;

    public UserDailySignupDto(String date, Long count, Long total) {
        this.date = date;
        this.count = count;
        this.total = total;
    }
}