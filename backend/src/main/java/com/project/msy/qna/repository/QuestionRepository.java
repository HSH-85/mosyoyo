package com.project.msy.qna.repository;

import com.project.msy.qna.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Long> {

    // 내가 작성한 질문 전체 조회
    List<Question> findAllByUserId(Long userId);

    // 상품에 대한 질문 조회 (Product 객체의 id 필드를 기준으로!)
    List<Question> findAllByProductId(Long productId);  // ✅ 중요

    // 시설에 대한 질문 조회 (Long 필드 그대로 사용 가능)
    List<Question> findAllByFacilityId(Long facilityId);
}
