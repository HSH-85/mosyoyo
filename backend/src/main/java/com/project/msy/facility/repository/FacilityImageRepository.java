package com.project.msy.facility.repository;

import com.project.msy.facility.entity.FacilityImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FacilityImageRepository extends JpaRepository<FacilityImage, Long> {
    // 특정 시설의 이미지들을 가져오기 위한 메서드
    List<FacilityImage> findByFacilityId(Long facilityId);

}
