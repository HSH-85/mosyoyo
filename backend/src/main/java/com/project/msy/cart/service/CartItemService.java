package com.project.msy.cart.service;

import com.project.msy.cart.dto.CartItemRequest;
import com.project.msy.cart.dto.CartItemResponse;
import com.project.msy.cart.entity.CartItem;
import com.project.msy.cart.repository.CartItemRepository;
import com.project.msy.product.entity.Product;
import com.project.msy.product.repository.ProductRepository;
import com.project.msy.user.entity.User;
import com.project.msy.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartItemService {

    private final CartItemRepository cartItemRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    @Transactional
    public void addItemToCart(CartItemRequest request) {
        User user = userRepository.findByUserId(request.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new IllegalArgumentException("상품을 찾을 수 없습니다."));

        cartItemRepository.findByUser_IdAndProduct_Id(user.getId(), product.getId())
                .ifPresentOrElse(item -> {
                    item.setQuantity(item.getQuantity() + request.getQuantity());
                    cartItemRepository.save(item);
                }, () -> {
                    CartItem item = new CartItem();
                    item.setUser(user);
                    item.setProduct(product);
                    item.setQuantity(request.getQuantity());
                    cartItemRepository.save(item);
                });
    }

    @Transactional(readOnly = true)
    public List<CartItemResponse> getCartItemsForUser(String userId) {
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        return cartItemRepository.findByUser_Id(user.getId())
                .stream()
                .map(item -> {
                    Product p = item.getProduct();
                    BigDecimal price = p.getDiscountPrice() != null
                            ? p.getDiscountPrice()
                            : p.getPrice();
                    return new CartItemResponse(
                            p.getId(),
                            p.getName(),
                            p.getImageUrls(),
                            item.getQuantity(),
                            price
                    );
                })
                .collect(Collectors.toList());
    }

    @Transactional
    public void updateItemQuantity(CartItemRequest request) {
        User user = userRepository.findByUserId(request.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        CartItem item = cartItemRepository
                .findByUser_IdAndProduct_Id(user.getId(), request.getProductId())
                .orElseThrow(() -> new IllegalArgumentException("장바구니 항목을 찾을 수 없습니다."));
        item.setQuantity(request.getQuantity());
        cartItemRepository.save(item);
    }

    @Transactional
    public void removeItemFromCart(Long productId, String userId) {
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        cartItemRepository.findByUser_IdAndProduct_Id(user.getId(), productId)
                .ifPresent(cartItemRepository::delete);
    }
    @Transactional
    public void clearCart(String userId) {
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        cartItemRepository.deleteByUser(user);
    }
}
