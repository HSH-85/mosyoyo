package com.project.msy.qna.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

/**
 * 관리자 답변 등록/수정 요청 DTO
 */
@Getter
@Setter
public class AnswerRequest {
    @NotBlank(message = "답변 내용을 입력해주세요")
    private String content;
}