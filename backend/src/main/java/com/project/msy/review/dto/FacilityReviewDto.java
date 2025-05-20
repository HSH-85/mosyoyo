package com.project.msy.review.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FacilityReviewDto {
    private Long id;
    private Long facilityId;
    private String userName;
    private Integer rating;
    private String content;
    private LocalDateTime createdAt;
}
