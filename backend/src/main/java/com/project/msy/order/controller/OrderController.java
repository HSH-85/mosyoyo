package com.project.msy.order.controller;

import com.project.msy.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/orders")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class OrderController {

    private final OrderService orderService;

    @GetMapping("/summary")
    public ResponseEntity<Map<String, Object>> getOrderSummary() {
        Map<String, Object> response = new HashMap<>();
        response.put("totalSales", orderService.getTotalSales());
        response.put("orderCount", orderService.getOrderCount());
        response.put("orders", orderService.getOrderSummaries());
        return ResponseEntity.ok(response);
    }
}
