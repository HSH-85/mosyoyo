package com.project.msy.review.controller;

import com.project.msy.review.dto.FacilityReviewDto;
import com.project.msy.review.service.FacilityReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/facility-reviews")
@RequiredArgsConstructor
public class FacilityReviewController {

    private final FacilityReviewService reviewService;

    @GetMapping("/{facilityId}")
    public ResponseEntity<List<FacilityReviewDto>> getReviews(@PathVariable Long facilityId) {
        return ResponseEntity.ok(reviewService.getReviewsByFacilityId(facilityId));
    }

    @PostMapping
    public ResponseEntity<FacilityReviewDto> createReview(
            @AuthenticationPrincipal(expression = "id") Long userId, // ✅ 인증된 사용자 ID
            @RequestBody FacilityReviewDto dto
    ) {
        return ResponseEntity.ok(reviewService.createReview(userId, dto));
    }

}
