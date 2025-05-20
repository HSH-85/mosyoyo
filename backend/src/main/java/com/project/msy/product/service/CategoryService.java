// src/main/java/com/project/msy/product/service/CategoryService.java
package com.project.msy.product.service;

import com.project.msy.product.entity.ProductCategory;
import com.project.msy.product.repository.ProductCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final ProductCategoryRepository categoryRepository;

    public List<ProductCategory> getAllCategories() {
        return categoryRepository.findAll();
    }
}
