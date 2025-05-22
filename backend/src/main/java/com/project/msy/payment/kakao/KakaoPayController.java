package com.project.msy.payment.kakao;

import com.project.msy.cart.dto.CartItemResponse;
import com.project.msy.cart.service.CartItemService;
import com.project.msy.order.service.OrderService;
import com.project.msy.payment.kakao.dto.KakaoPayApproveResponse;
import com.project.msy.payment.kakao.dto.KakaoPayReadyResponse;
import com.project.msy.stats.service.StatsService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/payment/kakao")
@RequiredArgsConstructor
public class KakaoPayController {
    private final KakaoPayService kakaoPayService;
    private final OrderService orderService;
    private final StatsService statsService;
    private final CartItemService cartItemService;

    // 결제 준비(ready) 메서드는 기존 코드 유지
    @PostMapping("/ready")
    public Map<String, String> ready(
            @RequestParam Long orderId,
            @RequestParam String itemName,
            @RequestParam int totalAmount,
            HttpSession session,
            Authentication authentication
    ) {
        String userId = authentication.getName();

        KakaoPayReadyResponse resp = kakaoPayService.ready(orderId, userId, itemName, totalAmount);
        session.setAttribute("kakao_tid", resp.getTid());
        session.setAttribute("userId", userId);
        Map<String, String> result = new HashMap<>();
        result.put("redirectUrl", resp.getNextRedirectPcUrl());
        result.put("tid", resp.getTid());
        return result;
    }

    // 결제 승인(approve) 메서드 수정: 이미 승인된 거래(-702)인 경우에도 주문·통계 처리 후 응답
    @GetMapping("/approve")
    public ResponseEntity<Map<String, Object>> approve(
            @RequestParam("pg_token") String pgToken,
            @RequestParam("orderId") Long orderId,
            @RequestParam("tid") String tid,
            HttpSession session,
            Authentication authentication
    ) {
        System.out.println("[DEBUG] /approve 진입! orderId=" + orderId + ", pg_token=" + pgToken);
        String userId = authentication.getName();
        Map<String, Object> response = new HashMap<>();
        KakaoPayApproveResponse approveResp = null;
        boolean alreadyApproved = false;

        try {
            // 1) 카카오페이 승인 시도
            approveResp = kakaoPayService.approve(tid, orderId, userId, pgToken);
        } catch (HttpClientErrorException e) {
            String body = e.getResponseBodyAsString();
            // 이미 승인된 거래인 경우(-702)는 정상 플래그만 설정
            if (body.contains("\"code\":-702")) {
                alreadyApproved = true;
                response.put("message", "이미 결제된 거래입니다.");
            } else {
                // 그 외 에러는 그대로 던짐
                throw e;
            }
        }

        // 2) 장바구니 조회
        List<CartItemResponse> cartItems = cartItemService.getCartItemsForUser(userId);

        // 3) orders 기록 및 통계 기록, 장바구니 비우기 (for-each 사용하여 effectively final 문제 해결)
        for (CartItemResponse item : cartItems) {
            Long productId = item.getProductId();
            int qty = item.getQuantity();
            int unitPrice = item.getUnitPrice().intValue();
            int subTotal = unitPrice * qty;

            // 주문 저장
            orderService.recordOrder(userId, productId, qty, subTotal, approveResp);
            // 매출 통계 저장
            statsService.recordSale(productId, qty);
        }
        cartItemService.clearCart(userId);

        // 4) 결과 반환
        if (!alreadyApproved) {
            response.put("approveInfo", approveResp);
            response.put("message", "결제가 완료되었습니다.");
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping("/cancel")
    public String cancel() {
        return "결제가 취소되었습니다.";
    }

    @GetMapping("/fail")
    public String fail() {
        return "결제에 실패했습니다.";
    }
}