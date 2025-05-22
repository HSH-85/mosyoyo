package com.project.msy.review.controller;

import com.project.msy.review.dto.ProductReviewDto;
import com.project.msy.review.service.ProductReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ProductReviewController {

    private final ProductReviewService reviewService;

    // ✅ 로그인 사용자 ID를 기반으로 리뷰 등록
    @PostMapping
    public ResponseEntity<ProductReviewDto> create(
            @AuthenticationPrincipal(expression = "id") Long userId,
            @RequestBody ProductReviewDto dto
    ) {
        return ResponseEntity.ok(reviewService.createReview(userId, dto));
    }

    // ✅ 상품별 리뷰 조회
    @GetMapping("/product/{productId}")
    public ResponseEntity<List<ProductReviewDto>> getByProduct(@PathVariable Long productId) {
        return ResponseEntity.ok(reviewService.getReviewsByProductId(productId));
    }

    // ✅ 리뷰 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        reviewService.deleteReview(id);
        return ResponseEntity.noContent().build();
    }
}
