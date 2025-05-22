package com.project.msy.stats.service.impl;

import com.project.msy.stats.entity.Stats;
import com.project.msy.stats.repository.StatsRepository;
import com.project.msy.stats.service.StatsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class StatsServiceImpl implements StatsService {
    private final StatsRepository statsRepository;

    @Override
    @Transactional
    public void recordSale(Long productId, int quantity) {

        System.out.println("[DEBUG] recordSale 호출 → productId="
                + productId + ", qty=" + quantity);

        LocalDate today = LocalDate.now();
        // 키 구성
        Stats.StatsType type       = Stats.StatsType.SALE;
        Stats.TargetType target   = Stats.TargetType.PRODUCT;
        Stats.TimeUnit unit       = Stats.TimeUnit.DAY;

        // 기존 통계 조회 또는 새 엔티티 생성
        Stats stats = statsRepository
                .findByTypeAndTargetTypeAndTargetIdAndUnitAndStatDate(
                        type, target, productId, unit, today
                )
                .orElseGet(() -> Stats.builder()
                        .type(type)
                        .targetType(target)
                        .targetId(productId)
                        .unit(unit)
                        .statDate(today)
                        .value(BigDecimal.ZERO)
                        .createdAt(LocalDateTime.now())
                        .build()
                );

        // 누적
        stats.setValue(stats.getValue().add(BigDecimal.valueOf(quantity)));
        statsRepository.save(stats);
    }
}