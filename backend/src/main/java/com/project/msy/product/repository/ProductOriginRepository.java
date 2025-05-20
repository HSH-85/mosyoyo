// src/main/java/com/project/msy/product/repository/ProductOriginRepository.java
package com.project.msy.product.repository;

import com.project.msy.product.entity.ProductOrigin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductOriginRepository extends JpaRepository<ProductOrigin, Long> {
}
