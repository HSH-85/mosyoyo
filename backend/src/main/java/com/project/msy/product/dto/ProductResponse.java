// src/main/java/com/project/msy/product/dto/ProductResponse.java
package com.project.msy.product.dto;

import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductResponse {
    private Long id;
    private String name;
    private Long category;
    private String categoryName;        // ← 추가
    private BigDecimal price;
    private BigDecimal discountPrice;
    private Integer stockQuantity;
    private String description;
    private BigDecimal shippingFee;
    private String manufacturer;
    private Long origin;
    private String originName;          // ← 추가
    private LocalDateTime createdAt;
    private List<SpecificationDto> specifications;
    private List<String> features;
    private List<String> images;
}
