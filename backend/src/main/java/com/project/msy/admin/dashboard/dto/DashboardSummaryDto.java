package com.project.msy.admin.dashboard.dto;

import lombok.Builder;
import lombok.Getter;
import java.math.BigDecimal;

@Getter
@Builder
public class DashboardSummaryDto {
    private long totalOrders;
    private long todayOrders;
    private BigDecimal totalRevenue;
}