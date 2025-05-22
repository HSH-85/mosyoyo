package com.project.msy.qna.service;

import com.project.msy.facility.entity.Facility;
import com.project.msy.facility.repository.FacilityRepository;
import com.project.msy.product.entity.Product;
import com.project.msy.product.repository.ProductRepository;
import com.project.msy.qna.dto.AnswerResponse;
import com.project.msy.qna.dto.QuestionRequest;
import com.project.msy.qna.dto.QuestionResponse;
import com.project.msy.qna.entity.Answer;
import com.project.msy.qna.entity.Question;
import com.project.msy.qna.exception.QuestionNotFoundException;
import com.project.msy.qna.repository.AnswerRepository;
import com.project.msy.qna.repository.QuestionRepository;
import com.project.msy.user.entity.User;
import com.project.msy.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class QnaServiceImpl implements QnaService {

    private final QuestionRepository questionRepo;
    private final AnswerRepository answerRepo;
    private final UserRepository userRepo;
    private final ProductRepository productRepo;
    private final FacilityRepository facilityRepo; // ✅ 추가

    @Override
    public QuestionResponse createQuestion(Long userId, QuestionRequest dto) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("사용자를 찾을 수 없습니다."));

        Product product = null;
        if (dto.getProductId() != null) {
            product = productRepo.findById(dto.getProductId())
                    .orElseThrow(() -> new EntityNotFoundException("상품을 찾을 수 없습니다."));
        }

        Facility facility = null;
        if (dto.getFacilityId() != null) {
            facility = facilityRepo.findById(dto.getFacilityId())
                    .orElseThrow(() -> new EntityNotFoundException("시설을 찾을 수 없습니다."));
        }

        Question q = new Question(dto.getTitle(), dto.getContent(), user, product, facility);
        questionRepo.save(q);
        return new QuestionResponse(q);
    }

    @Override
    @Transactional(readOnly = true)
    public List<QuestionResponse> getUserQuestions(Long userId) {
        return questionRepo.findAllByUserId(userId).stream()
                .map(QuestionResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public QuestionResponse getQuestion(Long userId, Long questionId) {
        Question q = questionRepo.findById(questionId)
                .orElseThrow(() -> new QuestionNotFoundException(questionId));
        if (!q.getUser().getId().equals(userId)) {
            throw new AccessDeniedException("본인의 질문만 조회할 수 있습니다.");
        }
        return new QuestionResponse(q);
    }

    @Override
    @Transactional(readOnly = true)
    public QuestionResponse getQuestionById(Long questionId) {
        Question q = questionRepo.findById(questionId)
                .orElseThrow(() -> new QuestionNotFoundException(questionId));
        return new QuestionResponse(q);
    }

    @Override
    @Transactional(readOnly = true)
    public List<QuestionResponse> getAllQuestions() {
        return questionRepo.findAll().stream()
                .map(QuestionResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public QuestionResponse updateQuestion(Long userId, Long questionId, QuestionRequest dto) {
        Question q = questionRepo.findById(questionId)
                .orElseThrow(() -> new QuestionNotFoundException(questionId));
        if (!q.getUser().getId().equals(userId)) {
            throw new AccessDeniedException("본인의 질문만 수정할 수 있습니다.");
        }
        q.setTitle(dto.getTitle());
        q.setContent(dto.getContent());
        return new QuestionResponse(q);
    }

    @Override
    public void deleteQuestion(Long userId, Long questionId) {
        Question q = questionRepo.findById(questionId)
                .orElseThrow(() -> new QuestionNotFoundException(questionId));
        if (!q.getUser().getId().equals(userId)) {
            throw new AccessDeniedException("본인의 질문만 삭제할 수 있습니다.");
        }
        questionRepo.delete(q);
    }

    @Override
    public AnswerResponse createAnswer(Long questionId, String content) {
        Question q = questionRepo.findById(questionId)
                .orElseThrow(() -> new EntityNotFoundException("질문이 없습니다."));
        Answer a = new Answer(content, q);
        answerRepo.save(a);
        return new AnswerResponse(a);
    }

    @Override
    @Transactional(readOnly = true)
    public AnswerResponse getAnswer(Long questionId) {
        Answer a = answerRepo.findByQuestionId(questionId)
                .orElseThrow(() -> new EntityNotFoundException("답변이 없습니다."));
        return new AnswerResponse(a);
    }

    @Override
    public AnswerResponse updateAnswer(Long questionId, String content) {
        Answer a = answerRepo.findByQuestionId(questionId)
                .orElseThrow(() -> new EntityNotFoundException("답변이 없습니다."));
        a.setContent(content);
        return new AnswerResponse(a);
    }

    @Override
    public void deleteAnswer(Long questionId) {
        Answer a = answerRepo.findByQuestionId(questionId)
                .orElseThrow(() -> new EntityNotFoundException("답변이 없습니다."));
        answerRepo.delete(a);
    }

    @Override
    public void deleteQuestionByAdmin(Long id) {
        Question q = questionRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("질문을 찾을 수 없습니다."));
        questionRepo.delete(q);
    }

    @Override
    @Transactional(readOnly = true)
    public List<QuestionResponse> getQuestionsByProduct(Long productId) {
        return questionRepo.findAllByProductId(productId).stream()
                .map(QuestionResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<QuestionResponse> getQuestionsByFacility(Long facilityId) {
        return questionRepo.findAllByFacilityId(facilityId).stream()
                .map(QuestionResponse::new)
                .collect(Collectors.toList());
    }
    @Override
    @Transactional(readOnly = true)
    public List<QuestionResponse> getQuestionsByType(String type) {
        List<Question> all = questionRepo.findAll();

        return all.stream()
                .filter(q -> {
                    if ("product".equalsIgnoreCase(type)) {
                        return q.getProduct() != null;
                    } else if ("facility".equalsIgnoreCase(type)) {
                        return q.getFacility() != null;
                    } else {
                        return false;
                    }
                })
                .map(QuestionResponse::new)
                .collect(Collectors.toList());
    }

}
