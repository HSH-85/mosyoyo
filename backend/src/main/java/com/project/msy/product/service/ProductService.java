// src/main/java/com/project/msy/product/service/ProductService.java
package com.project.msy.product.service;

import com.project.msy.product.dto.*;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ProductService {
    ProductResponse createProduct(CreateProductRequest request);
    List<ProductResponse> getAllProducts();

    @Transactional(readOnly = true)
    ProductResponse getProductById(Long productId);

    ProductResponse updateProduct(Long productId, UpdateProductRequest request);
    void deleteProduct(Long productId);// ← 추가
    void addImage(Long productId, String imageUrl);
}
