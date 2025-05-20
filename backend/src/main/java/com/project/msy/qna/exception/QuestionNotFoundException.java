// src/main/java/com/project/msy/qna/exception/QuestionNotFoundException.java
package com.project.msy.qna.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * QnA 질문을 찾을 수 없을 때 던지는 예외
 */
@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class QuestionNotFoundException extends RuntimeException {
    public QuestionNotFoundException(Long questionId) {
        super("질문이 없습니다: id=" + questionId);
    }
}
