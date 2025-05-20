package com.project.msy.qna.dto;

import lombok.Getter;
import com.project.msy.qna.entity.Answer;
import java.time.LocalDateTime;

/**
 * 답변 응답 DTO
 */
@Getter
public class AnswerResponse {
    private Long id;
    private Long questionId;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public AnswerResponse(Answer a) {
        this.id = a.getId();
        this.questionId = a.getQuestion().getId();
        this.content = a.getContent();
        this.createdAt = a.getCreatedAt();
        this.updatedAt = a.getUpdatedAt();
    }
}