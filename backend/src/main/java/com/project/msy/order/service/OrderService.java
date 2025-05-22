package com.project.msy.order.service;

import com.project.msy.order.dto.OrderSummaryDto;
import com.project.msy.payment.kakao.dto.KakaoPayApproveResponse;

import java.math.BigDecimal;
import java.util.List;

public interface OrderService {
    /**
     * 결제 승인 후 주문 기록
     * @return 저장된 주문 PK
     */
    Long recordOrder(
            String userId,
            Long productId,
            int quantity,
            int totalAmount,
            KakaoPayApproveResponse approveResponse
    );

    BigDecimal getTotalSales();

    Long getOrderCount();

    List<OrderSummaryDto> getOrderSummaries();
}