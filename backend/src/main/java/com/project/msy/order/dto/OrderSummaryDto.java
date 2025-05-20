package com.project.msy.order.dto;

import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
public class OrderSummaryDto {
    private Long orderId;
    private String userName;
    private String productName;
    private int quantity;
    private BigDecimal totalPrice;
    private LocalDateTime orderDate;

    public OrderSummaryDto(Long orderId, String userName, String productName,
                           int quantity, BigDecimal totalPrice, LocalDateTime orderDate) {
        this.orderId = orderId;
        this.userName = userName;
        this.productName = productName;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
        this.orderDate = orderDate;
    }

    // Getter 생략 or Lombok 사용 가능
}
