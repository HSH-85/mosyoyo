package com.project.msy.facility.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "facilities_price")
public class FacilityPrice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "f_id", nullable = false)
    private Long facilityId;

    @Column(name = "base_fee", nullable = false, precision = 10, scale = 2)
    private BigDecimal baseFee;

    @Column(name = "care_fee", nullable = false, precision = 10, scale = 2)
    private BigDecimal careFee;

    @Column(name = "meal_fee", nullable = false, precision = 10, scale = 2)
    private BigDecimal mealFee;

    @Column(name = "program_fee", nullable = false, precision = 10, scale = 2)
    private BigDecimal programFee;

    @Column(name = "insurance_rate", nullable = false, precision = 5, scale = 2)
    private BigDecimal insuranceRate;

    @Column(name = "total_fee", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalFee;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false, nullable = false)
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "f_id", insertable = false, updatable = false)
    private Facility facility;
}
