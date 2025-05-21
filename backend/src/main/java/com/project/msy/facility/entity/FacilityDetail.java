package com.project.msy.facility.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * 시설 상세 정보 Entity
 */
@Entity
@Table(name = "facility_details")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FacilityDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "f_id", nullable = false)
    private Facility facility;

    @Enumerated(EnumType.STRING)
    @Column(length = 2)
    private RatingGrade rating;

    @Column(name = "total_beds", nullable = false)
    private Integer totalBeds;

    @Column(name = "general_beds", nullable = false)
    private Integer generalBeds;

    @Column(name = "special_beds", nullable = false)
    private Integer specialBeds;

    @Column(name = "isolation_beds", nullable = false)
    private Integer isolationBeds;

    /**
     * 평가 등급 Enum
     */
    public enum RatingGrade {
        A, B, C, D
    }
}
