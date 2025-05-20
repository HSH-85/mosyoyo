package com.project.msy.admin.dashboard.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FacilityTypeDto {
    private String type;  // 시설 유형
    private long count;   // 해당 유형 시설 수
}