package com.project.msy.facility.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class FacilityResponseDTO {
    private Long id;
    private String type;
    private String name;
    private Integer establishedYear;
    private String address;
    private String phone;
    private String homepage;
    private String grade;
    private String description;
    private String weekdayHours;
    private String weekendHours;
    private String holidayHours;
    private String visitingHours;
    private String facilitySize;
    private List<String> imageUrls;
    private LocalDateTime createdAt;
    private Integer viewCount;
    private Integer likeCount;
}