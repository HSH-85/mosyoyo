package com.project.msy.admin.dashboard.service;

import com.project.msy.admin.dashboard.dto.*;
import com.project.msy.order.repository.OrderRepository;
import com.project.msy.product.repository.ProductRepository;
import com.project.msy.stats.repository.StatsRepository;
import com.project.msy.stats.entity.Stats;
import com.project.msy.user.repository.UserRepository;
import com.project.msy.facility.repository.FacilityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import com.project.msy.dashboard.dto.DailySaleDto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {

    private final OrderRepository orderRepo;
    private final StatsRepository statsRepo;
    private final UserRepository userRepo;
    private final FacilityRepository facilityRepo;
    private final ProductRepository productRepo;

    @Override
    public long getTotalFacilityCount() {
        return facilityRepo.count();
    }

    @Override
    public List<UserDailySignupDto> getDailyUserGrowth() {
        return userRepo.findDailySignupsForLast30Days();
    }

    @Override
    public List<DailySaleDto> getLast7DaysSales() {
        LocalDate start = LocalDate.now().minusDays(6);
        return orderRepo.findDailyRevenueSince(start).stream()
                .map(arr -> {
                    // 1) 날짜 처리
                    Object dateObj = arr[0];
                    LocalDate localDate;
                    if (dateObj instanceof java.sql.Date) {
                        localDate = ((java.sql.Date) dateObj).toLocalDate();
                    } else if (dateObj instanceof LocalDate) {
                        localDate = (LocalDate) dateObj;
                    } else {
                        // 혹시 문자열로 올 경우
                        localDate = LocalDate.parse(dateObj.toString());
                    }

                    // 2) 금액 처리
                    Object amtObj = arr[1];
                    long amount = (amtObj instanceof Number)
                            ? ((Number) amtObj).longValue()
                            : Long.parseLong(amtObj.toString());

                    // 3) DTO 생성
                    return new DailySaleDto(localDate.toString(), amount);
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<PopularFacilityDto> getPopularFacilities() {
        return facilityRepo.findTop5WithReviewCount().stream()
                .map(arr -> new PopularFacilityDto(
                        // Object[] 순서: id, name, type, viewCount, likeCount, reviewCount
                        ((Number)arr[0]).longValue(),
                        (String) arr[1],
                        (String) arr[2],
                        ((Number)arr[3]).intValue(),
                        ((Number)arr[4]).intValue(),
                        ((Number)arr[5]).intValue()    // 리뷰 수
                ))
                .collect(Collectors.toList());
    }

    @Override
    public List<PopularProductDto> getPopularProducts() {
        return productRepo.findTop5ByOrderVolume().stream()
                .map(arr -> new PopularProductDto(
                        ((Number)arr[0]).longValue(),
                        (String) arr[1],
                        ((Number)arr[2]).longValue(),
                        (BigDecimal)arr[3],
                        ((Number)arr[4]).intValue(),
                        (String) arr[5]
                ))
                .collect(Collectors.toList());
    }

    @Override
    public List<FacilityTypeStatDto> getFacilityTypeStats() {
        return facilityRepo.countByType().stream()
                .map(arr -> new FacilityTypeStatDto(
                        (String) arr[0],
                        ((Number) arr[1]).longValue()
                ))
                .collect(Collectors.toList());
    }
}