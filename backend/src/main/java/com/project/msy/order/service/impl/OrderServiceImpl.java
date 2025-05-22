package com.project.msy.order.service.impl;

import com.project.msy.order.dto.OrderSummaryDto;
import com.project.msy.order.entity.Order;
import com.project.msy.order.repository.OrderRepository;
import com.project.msy.order.service.OrderService;
import com.project.msy.payment.kakao.dto.KakaoPayApproveResponse;
import com.project.msy.user.entity.User;
import com.project.msy.user.repository.UserRepository;
import com.project.msy.product.entity.Product;
import com.project.msy.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    @Override
    @Transactional
    public Long recordOrder(
            String userId,
            Long productId,
            int quantity,
            int totalAmount,
            KakaoPayApproveResponse approveResponse
    ) {
        // 1) 회원, 상품 로딩
        User userEntity = userRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("해당 유저를 찾을 수 없습니다: " + userId));

        Product productEntity = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("해당 상품을 찾을 수 없습니다: " + productId));

        // 2) 재고 검증 & 차감
        if (productEntity.getStockQuantity() < quantity) {
            throw new IllegalArgumentException("재고가 부족합니다. 현재 재고: "
                    + productEntity.getStockQuantity()
                    + ", 주문 수량: " + quantity);
        }
        productEntity.setStockQuantity(productEntity.getStockQuantity() - quantity);
        // 저장은 생략해도 Transactional 커밋 시점에 flush 되어 반영됩니다.
        // productRepository.save(productEntity);

        // 3) 주문 생성
        Order order = Order.builder()
                .user(userEntity)
                .product(productEntity)
                .quantity(quantity)
                .totalPrice(BigDecimal.valueOf(totalAmount))
                .status(Order.OrderStatus.결제완료)
                .build();

        // 4) 주문 저장 및 ID 리턴
        return orderRepository.save(order).getId();
    }
    @Override
    public BigDecimal getTotalSales() {
        return orderRepository.findTotalSales() != null ? orderRepository.findTotalSales() : BigDecimal.ZERO;
    }

    @Override
    public Long getOrderCount() {
        return orderRepository.countOrders();
    }

    @Override
    public List<OrderSummaryDto> getOrderSummaries() {
        return orderRepository.findOrderSummaries();
    }


}