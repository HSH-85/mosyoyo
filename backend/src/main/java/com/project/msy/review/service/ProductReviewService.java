package com.project.msy.review.service;

import com.project.msy.review.dto.ProductReviewDto;

import java.util.List;

public interface ProductReviewService {
    ProductReviewDto createReview(Long userId, ProductReviewDto dto); // 🔁 userId 추가
    List<ProductReviewDto> getReviewsByProductId(Long productId);
    void deleteReview(Long reviewId);
}
