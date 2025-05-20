package com.project.msy.cart.controller;

import com.project.msy.cart.dto.CartItemRequest;
import com.project.msy.cart.dto.CartItemResponse;
import com.project.msy.cart.service.CartItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartItemController {

    private final CartItemService cartItemService;

    // 장바구니에 추가
    @PostMapping
    public ResponseEntity<?> addToCart(@RequestBody CartItemRequest request,
                                       Authentication authentication) {
        String userId = authentication.getName();
        request.setUserId(userId);
        cartItemService.addItemToCart(request);
        return ResponseEntity.ok("장바구니에 추가되었습니다.");
    }

    // 장바구니 조회
    @GetMapping
    public ResponseEntity<List<CartItemResponse>> getCartItems(Authentication authentication) {
        String userId = authentication.getName();
        List<CartItemResponse> cart = cartItemService.getCartItemsForUser(userId);
        return ResponseEntity.ok(cart);
    }

    // 수량 변경
    @PutMapping
    public ResponseEntity<?> updateItem(@RequestBody CartItemRequest request,
                                        Authentication authentication) {
        String userId = authentication.getName();
        request.setUserId(userId);
        cartItemService.updateItemQuantity(request);
        return ResponseEntity.ok("수량이 업데이트되었습니다.");
    }

    // 항목 삭제
    @DeleteMapping("/{productId}")
    public ResponseEntity<?> removeItem(@PathVariable Long productId,
                                        Authentication authentication) {
        String userId = authentication.getName();
        cartItemService.removeItemFromCart(productId, userId);
        return ResponseEntity.ok("장바구니에서 삭제되었습니다.");
    }

    @DeleteMapping
    public ResponseEntity<?> clearCart(Authentication authentication) {
        String userId = authentication.getName();
        cartItemService.clearCart(userId);
        return ResponseEntity.ok("장바구니 전체 삭제되었습니다.");
    }
}
