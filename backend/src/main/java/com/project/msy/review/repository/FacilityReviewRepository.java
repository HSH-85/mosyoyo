package com.project.msy.review.repository;

import com.project.msy.review.entity.FacilityReview;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FacilityReviewRepository extends JpaRepository<FacilityReview, Long> {
    List<FacilityReview> findByFacilityId(Long facilityId);
}
