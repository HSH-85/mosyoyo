package com.project.msy.qna.controller;

import com.project.msy.qna.dto.QuestionRequest;
import com.project.msy.qna.dto.QuestionResponse;
import com.project.msy.qna.dto.AnswerRequest;
import com.project.msy.qna.dto.AnswerResponse;
import com.project.msy.qna.service.QnaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/questions")
@RequiredArgsConstructor
public class QnaController {

    private final QnaService service;

    // ===== 유저용 =====

    @PostMapping
    public ResponseEntity<QuestionResponse> createQuestion(
            @AuthenticationPrincipal(expression = "id") Long userId,
            @Valid @RequestBody QuestionRequest dto
    ) {
        QuestionResponse created = service.createQuestion(userId, dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/my")
    public ResponseEntity<List<QuestionResponse>> getMyQuestions(
            @AuthenticationPrincipal(expression = "id") Long userId
    ) {
        List<QuestionResponse> list = service.getUserQuestions(userId);
        return ResponseEntity.ok(list);
    }

    @GetMapping("/my/{id}")
    public ResponseEntity<QuestionResponse> getMyQuestionById(
            @AuthenticationPrincipal(expression = "id") Long userId,
            @PathVariable Long id
    ) {
        QuestionResponse resp = service.getQuestion(userId, id);
        return ResponseEntity.ok(resp);
    }

    @PutMapping("/my/{id}")
    public ResponseEntity<QuestionResponse> updateMyQuestion(
            @AuthenticationPrincipal(expression = "id") Long userId,
            @PathVariable Long id,
            @Valid @RequestBody QuestionRequest dto
    ) {
        QuestionResponse updated = service.updateQuestion(userId, id, dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/my/{id}")
    public ResponseEntity<Void> deleteMyQuestion(
            @AuthenticationPrincipal(expression = "id") Long userId,
            @PathVariable Long id
    ) {
        service.deleteQuestion(userId, id);
        return ResponseEntity.noContent().build();
    }

    // ===== 관리자 전용 =====

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<QuestionResponse>> getAllQuestions() {
        return ResponseEntity.ok(service.getAllQuestions());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<QuestionResponse> getQuestionById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getQuestionById(id));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{questionId}/answer")
    public ResponseEntity<AnswerResponse> createAnswer(
            @PathVariable Long questionId,
            @Valid @RequestBody AnswerRequest dto
    ) {
        AnswerResponse created = service.createAnswer(questionId, dto.getContent());
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{questionId}/answer")
    public ResponseEntity<AnswerResponse> getAnswer(@PathVariable Long questionId) {
        return ResponseEntity.ok(service.getAnswer(questionId));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{questionId}/answer")
    public ResponseEntity<AnswerResponse> updateAnswer(
            @PathVariable Long questionId,
            @Valid @RequestBody AnswerRequest dto
    ) {
        return ResponseEntity.ok(service.updateAnswer(questionId, dto.getContent()));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{questionId}/answer")
    public ResponseEntity<Void> deleteAnswer(@PathVariable Long questionId) {
        service.deleteAnswer(questionId);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteQuestionByAdmin(@PathVariable Long id) {
        service.deleteQuestionByAdmin(id);
        return ResponseEntity.noContent().build();
    }

    // ===== 추가된 상품/시설별 조회 =====

    @GetMapping("/product/{productId}")
    public ResponseEntity<List<QuestionResponse>> getQuestionsByProduct(@PathVariable Long productId) {
        return ResponseEntity.ok(service.getQuestionsByProduct(productId));
    }

    @GetMapping("/facility/{facilityId}")
    public ResponseEntity<List<QuestionResponse>> getQuestionsByFacility(@PathVariable Long facilityId) {
        return ResponseEntity.ok(service.getQuestionsByFacility(facilityId));
    }
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/type/{type}")
    public ResponseEntity<List<QuestionResponse>> getQuestionsByType(@PathVariable String type) {
        return ResponseEntity.ok(service.getQuestionsByType(type));
    }

}
