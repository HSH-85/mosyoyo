package com.project.msy.review.service;

import com.project.msy.product.entity.Product;
import com.project.msy.product.repository.ProductRepository;
import com.project.msy.review.dto.ProductReviewDto;
import com.project.msy.review.entity.ProductReview;
import com.project.msy.review.repository.ProductReviewRepository;
import com.project.msy.user.entity.User;
import com.project.msy.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductReviewServiceImpl implements ProductReviewService {

    private final ProductReviewRepository reviewRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository; // ✅ 사용자 정보 조회용 추가

    @Override
    public ProductReviewDto createReview(Long userId, ProductReviewDto dto) {
        Product product = productRepository.findById(dto.getProductId())
                .orElseThrow(() -> new RuntimeException("상품을 찾을 수 없습니다."));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        ProductReview review = ProductReview.builder()
                .product(product)
                .userName(user.getUserId()) // ✅ DB에 저장된 유저 ID 문자열
                .rating(dto.getRating())
                .content(dto.getContent())
                .build();

        return toDto(reviewRepository.save(review));
    }

    @Override
    public List<ProductReviewDto> getReviewsByProductId(Long productId) {
        return reviewRepository.findByProductIdOrderByCreatedAtAsc(productId)
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteReview(Long reviewId) {
        reviewRepository.deleteById(reviewId);
    }

    private ProductReviewDto toDto(ProductReview entity) {
        return ProductReviewDto.builder()
                .id(entity.getId())
                .productId(entity.getProduct().getId())
                .userName(entity.getUserName())
                .rating(entity.getRating())
                .content(entity.getContent())
                .createdAt(entity.getCreatedAt())
                .build();
    }
}
