package com.project.msy.qna.repository;

import com.project.msy.qna.entity.Answer;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface AnswerRepository extends JpaRepository<Answer, Long> {
    Optional<Answer> findByQuestionId(Long questionId);
}
