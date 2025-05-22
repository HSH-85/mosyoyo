package com.project.msy.qna.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

/**
 * 상품 또는 시설 문의 등록용 DTO
 */
@Getter
@Setter
public class QuestionRequest {

    @NotBlank(message = "제목을 입력해주세요")
    private String title;

    @NotBlank(message = "내용을 입력해주세요")
    private String content;

    private Long productId;   // 상품 문의 시 사용
    private Long facilityId;  // 시설 문의 시 사용
}
