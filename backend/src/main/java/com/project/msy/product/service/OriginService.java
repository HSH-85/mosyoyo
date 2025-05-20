// src/main/java/com/project/msy/product/service/OriginService.java
package com.project.msy.product.service;

import com.project.msy.product.entity.ProductOrigin;
import com.project.msy.product.repository.ProductOriginRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OriginService {
    private final ProductOriginRepository originRepository;

    public List<ProductOrigin> getAllOrigins() {
        return originRepository.findAll();
    }
}
