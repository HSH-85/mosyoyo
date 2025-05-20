package com.project.msy.cart.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartItemRequest {
    private String userId;
    private Long productId;
    private int quantity;
}
