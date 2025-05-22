package com.project.msy.stats.repository;

import com.project.msy.stats.entity.Stats;
import com.project.msy.admin.dashboard.dto.MonthlyRevenueDto;
import com.project.msy.admin.dashboard.dto.ActivityDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface StatsRepository extends JpaRepository<Stats, Long> {
    @Query("SELECT COALESCE(SUM(s.value), 0) FROM Stats s WHERE s.type = :type")
    BigDecimal sumValueByType(@Param("type") Stats.StatsType type);

    @Query("SELECT COALESCE(SUM(s.value), 0) FROM Stats s " +
            "WHERE s.type = :type AND s.unit = :unit " +
            "AND FUNCTION('YEAR', s.statDate) = :year " +
            "AND FUNCTION('MONTH', s.statDate) = :month")
    BigDecimal sumValueByTypeAndMonth(@Param("type") Stats.StatsType type,
                                      @Param("unit") Stats.TimeUnit unit,
                                      @Param("year") int year,
                                      @Param("month") int month);

    @Query("SELECT new com.project.msy.admin.dashboard.dto.MonthlyRevenueDto(" +
            "CONCAT(YEAR(s.statDate), '-', " +
            "CASE WHEN MONTH(s.statDate) < 10 THEN CONCAT('0', MONTH(s.statDate)) ELSE STR(MONTH(s.statDate)) END), " +
            "SUM(s.value)) " +
            "FROM Stats s " +
            "WHERE s.type = 'SALE' AND s.unit = 'MONTH' " +
            "GROUP BY YEAR(s.statDate), MONTH(s.statDate) " +
            "ORDER BY YEAR(s.statDate), MONTH(s.statDate)")
    List<MonthlyRevenueDto> findMonthlyRevenue();



    @Query("SELECT new com.project.msy.admin.dashboard.dto.ActivityDto(" +
            "s.id, CONCAT(s.type, ' on ', s.statDate), s.createdAt) " +
            "FROM Stats s ORDER BY s.createdAt DESC")
    List<ActivityDto> findRecentStats(Pageable pageable);

    Optional<Stats> findByTypeAndTargetTypeAndTargetIdAndUnitAndStatDate(
            Stats.StatsType type,
            Stats.TargetType targetType,
            Long targetId,
            Stats.TimeUnit unit,
            LocalDate statDate
    );

    @Query("SELECT COALESCE(SUM(s.value), 0) FROM Stats s WHERE s.type = 'SALE'")
    BigDecimal sumRevenue();

    @Query("SELECT COALESCE(SUM(s.value), 0) FROM Stats s WHERE s.type = 'SALE' AND s.statDate BETWEEN :start AND :end")
    BigDecimal sumRevenueByDate(@Param("start") LocalDate start, @Param("end") LocalDate end);
}