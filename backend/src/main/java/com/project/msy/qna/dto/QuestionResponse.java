package com.project.msy.qna.dto;

import com.project.msy.qna.entity.Question;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class QuestionResponse {
    private Long id;
    private String title;
    private String content;
    private String userId;      // ✅ 사용자의 계정 ID (문자열)
    private Long userDbId;      // ✅ 사용자 PK (숫자 ID)
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private AnswerResponse answer;

    // ✅ 비교용 개별 ID
    private Long productId;
    private Long facilityId;

    // ✅ 공통된 질문 대상
    private String targetType;    // "product", "facility", or "none"
    private Long targetId;
    private String targetName;

    public QuestionResponse(Question q) {
        this.id = q.getId();
        this.title = q.getTitle();
        this.content = q.getContent();
        this.userId = q.getUser().getUserId();
        this.userDbId = q.getUser().getId();
        this.createdAt = q.getCreatedAt();
        this.updatedAt = q.getUpdatedAt();

        // ✅ 개별 ID (비교용)
        this.productId = q.getProduct() != null ? q.getProduct().getId() : null;
        this.facilityId = q.getFacility() != null ? q.getFacility().getId() : null;

        // ✅ 공통 구조
        if (q.getProduct() != null) {
            this.targetType = "product";
            this.targetId = q.getProduct().getId();
            this.targetName = q.getProduct().getName();
        } else if (q.getFacility() != null) {
            this.targetType = "facility";
            this.targetId = q.getFacility().getId();
            this.targetName = q.getFacility().getName();
        } else {
            this.targetType = "none";
            this.targetId = null;
            this.targetName = null;
        }

        if (q.getAnswer() != null) {
            this.answer = new AnswerResponse(q.getAnswer());
        }
    }
}
