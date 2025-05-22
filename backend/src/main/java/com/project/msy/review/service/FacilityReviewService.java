package com.project.msy.review.service;

import com.project.msy.review.dto.FacilityReviewDto;

import java.util.List;

public interface FacilityReviewService {
    List<FacilityReviewDto> getReviewsByFacilityId(Long facilityId);

    // ✅ 사용자 ID를 매개변수로 추가
    FacilityReviewDto createReview(Long userId, FacilityReviewDto dto);
}
