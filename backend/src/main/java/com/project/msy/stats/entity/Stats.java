package com.project.msy.stats.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 통계 정보 Entity
 */
@Entity
@Table(name = "stats")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Stats {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private StatsType type;

    @Enumerated(EnumType.STRING)
    @Column(name = "target_type", nullable = false, length = 10)
    private TargetType targetType;

    @Column(name = "target_id", nullable = false)
    private Long targetId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private TimeUnit unit;

    @Column(name = "stat_date", nullable = false)
    private LocalDate statDate;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal value;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    /**
     * 통계 종류
     */
    public enum StatsType {
        VIEW, LIKE, ORDER, SALE
    }

    /**
     * 타겟 종류
     */
    public enum TargetType {
        PRODUCT, FACILITY
    }

    /**
     * 단위 종류
     */
    public enum TimeUnit {
        DAY, WEEK, MONTH, YEAR
    }
}
