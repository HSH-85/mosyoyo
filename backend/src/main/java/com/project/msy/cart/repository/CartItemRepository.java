package com.project.msy.cart.repository;

import com.project.msy.cart.entity.CartItem;
import com.project.msy.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    Optional<CartItem> findByUser_IdAndProduct_Id(Long userId, Long productId);
    List<CartItem> findByUser_Id(Long userId);
    void deleteByUser(User user);
}
