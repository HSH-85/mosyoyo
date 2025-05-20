package com.project.msy.review.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductReviewDto {
    private Long id;
    private Long productId;
    private String userId; // ✅ 사용자 이름 대신 로그인 ID 표시
    private String userName;
    private Integer rating;
    private String content;
    private LocalDateTime createdAt;
}
