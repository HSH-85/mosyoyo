package com.project.msy.dashboard.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 일별 매출 응답용 DTO
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DailySaleDto {
    /**
     * 날짜 문자열 (예: "2025-05-14")
     */
    private String date;

    /**
     * 해당 날짜 매출 합계
     */
    private long amount;
}