// src/main/java/com/project/msy/product/repository/ProductCategoryRepository.java
package com.project.msy.product.repository;

import com.project.msy.product.entity.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductCategoryRepository extends JpaRepository<ProductCategory, Long> {
}
