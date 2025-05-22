package com.project.msy.stats.service;

public interface StatsService {
    /**
     * 판매량 통계 기록 (stats.type=SALE)
     */
    void recordSale(Long productId, int quantity);
}