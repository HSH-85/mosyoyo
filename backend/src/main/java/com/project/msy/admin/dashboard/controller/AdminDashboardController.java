package com.project.msy.admin.dashboard.controller;

import com.project.msy.admin.dashboard.dto.*;
import com.project.msy.admin.dashboard.service.DashboardService;
import com.project.msy.facility.repository.FacilityRepository;
import com.project.msy.order.repository.OrderRepository;
import com.project.msy.product.repository.ProductRepository;
import com.project.msy.stats.repository.StatsRepository;
import com.project.msy.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.project.msy.dashboard.dto.DailySaleDto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/dashboard")
@RequiredArgsConstructor
public class AdminDashboardController {

    private final DashboardService service;
    private final ProductRepository productRepository;
    private final StatsRepository statsRepository;
    private final OrderRepository orderRepository;
    private final FacilityRepository facilityRepo;
    private final UserRepository userRepo;

    @GetMapping("/facility-count")
    public long getFacilityCount() {
        return service.getTotalFacilityCount();
    }

    @GetMapping("/summary")
    public ResponseEntity<Map<String, Object>> getSummary() {
        Map<String, Object> summary = new HashMap<>();

        // ✅ 상품 수 집계
        Map<String, Object> productStats = new HashMap<>();
        long totalProducts = productRepository.count();
        long inStock = productRepository.countByStockQuantityGreaterThan(0);
        long outOfStock = totalProducts - inStock;

        productStats.put("total", totalProducts);
        productStats.put("inStock", inStock);
        productStats.put("outOfStock", outOfStock);

        summary.put("products", productStats);

        return ResponseEntity.ok(summary);
    }

    @GetMapping("/user-daily-growth")
    public ResponseEntity<List<UserDailySignupDto>> getUserDailyGrowth() {
        return ResponseEntity.ok(service.getDailyUserGrowth());
    }

    @GetMapping("/sale-summary")
    public ResponseEntity<Map<String, Object>> getSaleSummary() {
        Map<String, Object> summary = orderRepository.getSalesSummary();
        return ResponseEntity.ok(summary);
    }

    /** 최근 7일 일별 매출 */
    @GetMapping("/daily-sales")
    public List<DailySaleDto> dailySalesLast7() {
        return service.getLast7DaysSales();
    }

    /** 인기 시설 Top5 */
    @GetMapping("/popular-facilities")
    public List<PopularFacilityDto> popularFacilities() {
        return service.getPopularFacilities();
    }

    /** 인기 상품 Top5 */
    @GetMapping("/popular-products")
    public List<PopularProductDto> popularProducts() {
        return service.getPopularProducts();
    }

    @GetMapping("/facility-type-stats")
    public List<FacilityTypeStatDto> facilityTypeStats() {
        return service.getFacilityTypeStats();
    }

    @GetMapping("/total-summary")
    public ResponseEntity<Map<String,Object>> getTotalSummary() {
        // ── 오늘 00:00 ~ 내일 00:00 구간 계산
        LocalDateTime startOfDay     = LocalDate.now().atStartOfDay();
        LocalDateTime startOfTomorrow= startOfDay.plusDays(1);

        Map<String,Object> summary = new HashMap<>();

        // ── 시설 통계
        long totalFacilities = facilityRepo.count();
        long newFacilities   = facilityRepo.countByCreatedAtBetween(startOfDay, startOfTomorrow);

        Map<String,Long> facStats = new HashMap<>();
        facStats.put("total", totalFacilities);
        facStats.put("new",   newFacilities);
        summary.put("facilities", facStats);

        // ── 상품 통계
        long totalProducts = productRepository.count();
        long newProducts   = productRepository.countByCreatedAtBetween(startOfDay, startOfTomorrow);

        long inStock  = productRepository.countByStockQuantityGreaterThan(0);
        long outStock = totalProducts - inStock;

        Map<String,Long> prodStats = new HashMap<>();
        prodStats.put("total",    totalProducts);
        prodStats.put("new",      newProducts);
        prodStats.put("inStock",  inStock);
        prodStats.put("outOfStock", outStock);
        summary.put("products", prodStats);

        // ── 사용자 통계
        long totalUsers = userRepo.count();
        long newUsers   = userRepo.countByCreatedAtBetween(startOfDay, startOfTomorrow);

        Map<String,Long> userStats = new HashMap<>();
        userStats.put("total", totalUsers);
        userStats.put("new",   newUsers);
        summary.put("users", userStats);

        // ── (필요하다면) 주문/매출 등 추가 통계도 같은 패턴으로 …

        return ResponseEntity.ok(summary);
    }
}