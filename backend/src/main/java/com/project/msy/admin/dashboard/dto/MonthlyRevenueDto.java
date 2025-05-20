package com.project.msy.admin.dashboard.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class MonthlyRevenueDto {
    private String month;  // 형식: "YYYY-MM"
    private BigDecimal revenue;

    public MonthlyRevenueDto(String month, BigDecimal revenue) {
        this.month = month;
        this.revenue = revenue;
    }
}