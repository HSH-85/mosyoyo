package com.project.msy.facility.repository;

import com.project.msy.admin.dashboard.dto.FacilityTypeDto;
import com.project.msy.facility.entity.Facility;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FacilityRepository extends JpaRepository<Facility, Long> {
    //    타입별로 조회
    List<Facility> findByType(String type);

    @Query("SELECT f FROM Facility f " +
            "WHERE (:type IS NULL OR f.type = :type) " +
            "AND (:facilitySize IS NULL OR f.facilitySize = :facilitySize) " +
            "AND (:grade IS NULL OR f.grade = :grade)")
    List<Facility> findByConditions(@Param("type") String type,
                                    @Param("facilitySize") String facilitySize,
                                    @Param("grade") String grade);
//    지역 세부 설정을 하기위한  jpa
public List<Facility> findByAddressContainingIgnoreCase(String region);

//    @Query("SELECT new com.project.msy.admin.dashboard.dto.FacilityTypeDto(f.type, COUNT(f)) " +
//            "FROM Facility f GROUP BY f.type")
//    List<FacilityTypeDto> countByType();

    // 조회수(viewCount) 내림차순 Top5
    @Query(value = """
      SELECT 
        f.id,
        f.name,
        f.type,
        IFNULL(f.view_count,0),
        IFNULL(f.like_count,0),
        IFNULL(COUNT(r.id),0) AS review_count
      FROM facilities f
      LEFT JOIN f_reviews r
        ON r.facility_id = f.id
      GROUP BY f.id, f.name, f.type, f.view_count, f.like_count
      ORDER BY f.view_count DESC
      LIMIT 5
    """, nativeQuery = true)
    List<Object[]> findTop5WithReviewCount();

    /**
     * 시설을 type별로 그룹핑하여 건수 조회
     */
    @Query("SELECT f.type, COUNT(f) FROM Facility f GROUP BY f.type")
    List<Object[]> countByType();
}
