package com.project.msy.qna.service;

import com.project.msy.qna.dto.AnswerResponse;
import com.project.msy.qna.dto.QuestionResponse;
import com.project.msy.qna.dto.QuestionRequest;

import java.util.List;

public interface QnaService {

    // 질문 생성 및 기본 조회
    QuestionResponse createQuestion(Long userId, QuestionRequest dto);
    List<QuestionResponse> getUserQuestions(Long userId);
    QuestionResponse getQuestion(Long userId, Long questionId);
    QuestionResponse getQuestionById(Long questionId);
    List<QuestionResponse> getAllQuestions();
    QuestionResponse updateQuestion(Long userId, Long questionId, QuestionRequest dto);
    void deleteQuestion(Long userId, Long questionId);

    // ✅ 상품별 질문 조회
    List<QuestionResponse> getQuestionsByProduct(Long productId);

    // ✅ 시설별 질문 조회
    List<QuestionResponse> getQuestionsByFacility(Long facilityId);

    List<QuestionResponse> getQuestionsByType(String type);

    // 답변 관련
    AnswerResponse createAnswer(Long questionId, String content);
    AnswerResponse getAnswer(Long questionId);
    AnswerResponse updateAnswer(Long questionId, String content);
    void deleteAnswer(Long questionId);

    // 관리자 질문 삭제
    void deleteQuestionByAdmin(Long id);
}
