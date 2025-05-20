// src/main/java/com/project/msy/product/controller/CategoryController.java
package com.project.msy.product.controller;

import com.project.msy.product.entity.ProductCategory;
import com.project.msy.product.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/categories", produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping
    public List<ProductCategory> list() {
        return categoryService.getAllCategories();
    }
}
