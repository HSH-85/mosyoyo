package com.project.msy.facility.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * 시설 의료진 정보 Entity
 */
@Entity
@Table(name = "facility_staff")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FacilityStaff {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "f_id", nullable = false)
    private Facility facility;

    @Column(nullable = false, length = 50)
    private String position;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(length = 100)
    private String specialty;
}
