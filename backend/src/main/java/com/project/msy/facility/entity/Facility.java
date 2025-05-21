package com.project.msy.facility.entity;

import com.project.msy.facility.entity.Specialization;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * 시설 기본 정보 Entity
 */
@Entity
@Table(name = "facilities")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Facility {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private FacilityType type;

    @Column(nullable = false, length = 255)
    private String name;

    @Column(name = "established_year", nullable = false)
    private Integer establishedYear;

    @Column(nullable = false, length = 500)
    private String address;

    @Column(nullable = false, length = 20)
    private String phone;

    @Column(nullable = false, length = 500)
    private String homepage;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String description;

    @Column(name = "weekday_hours", nullable = false, length = 50)
    private String weekdayHours;

    @Column(name = "weekend_hours", nullable = false, length = 50)
    private String weekendHours;

    @Column(name = "holiday_hours", length = 50)
    private String holidayHours;

    @Column(name = "visiting_hours", length = 50)
    private String visitingHours;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    /**
     * 특화 영역 연관 관계 (다대다)
     */
    @ManyToMany
    @JoinTable(
            name = "facility_specializations",
            joinColumns = @JoinColumn(name = "f_id"),
            inverseJoinColumns = @JoinColumn(name = "s_id")
    )
    private Set<Specialization> specializations = new HashSet<>();
}
