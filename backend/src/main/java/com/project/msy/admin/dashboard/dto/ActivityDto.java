package com.project.msy.admin.dashboard.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ActivityDto {
    private Long id;
    private String description;
    private LocalDateTime timestamp;
}