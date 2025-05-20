package com.project.msy.order.repository;

import com.project.msy.order.dto.OrderSummaryDto;
import com.project.msy.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    // 판매 현황용
    @Query("""
    SELECT new com.project.msy.order.dto.OrderSummaryDto(
        o.id, u.name, p.name, o.quantity, o.totalPrice, o.orderDate
    )
    FROM Order o
    JOIN o.user u
    JOIN o.product p
    ORDER BY o.orderDate DESC
    """)
    List<OrderSummaryDto> findOrderSummaries();

    @Query("SELECT SUM(o.totalPrice) FROM Order o")
    BigDecimal findTotalSales();

    @Query("SELECT COUNT(o) FROM Order o")
    Long countOrders();

    // 관리자 대시보드 통계용
    long countByOrderDateBetween(LocalDateTime start, LocalDateTime end);

    @Query(value = """
        SELECT
            SUM(total_price) AS total,
            SUM(CASE WHEN DATE(order_date) = CURDATE() THEN total_price ELSE 0 END) AS today,
            SUM(CASE WHEN DATE(order_date) = CURDATE() - INTERVAL 1 DAY THEN total_price ELSE 0 END) AS yesterday
        FROM orders
    """, nativeQuery = true)
    Map<String, Object> getSalesSummary();

    /**
     * 최근 7일(오늘 포함) 일별 매출 조회
     */
    @Query(value = """
      SELECT 
        DATE(o.order_date)   AS sale_date, 
        COALESCE(SUM(o.total_price), 0) AS amount
      FROM orders o
      WHERE o.order_date >= :startDate
      GROUP BY sale_date
      ORDER BY sale_date
    """, nativeQuery = true)
    List<Object[]> findDailyRevenueSince(@Param("startDate") LocalDate startDate);
}
