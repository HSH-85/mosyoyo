package com.project.msy.product.controller;

import com.project.msy.S3.S3Uploader;
import com.project.msy.product.dto.*;
import com.project.msy.product.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.constraints.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/products")
public class ProductController {
    private final ProductService productService;
    private final S3Uploader s3Uploader;

    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(
            @Valid @RequestPart("product") CreateProductRequest request,
            @RequestPart(value = "images", required = false) List<MultipartFile> imageFiles
    ) throws IOException {
        ProductResponse response = productService.createProduct(request);

        if (imageFiles != null) {
            for (MultipartFile file : imageFiles) {
                String url = s3Uploader.upload(file, "products");
                productService.addImage(response.getId(), url);
            }
        }
        return ResponseEntity.status(201).body(response);
    }

    @GetMapping
    public ResponseEntity<List<ProductResponse>> listAll() {
        List<ProductResponse> list = productService.getAllProducts();
        return ResponseEntity.ok(list);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> updateProduct(
            @PathVariable("id") Long id,
            @Valid @RequestBody UpdateProductRequest request
    ) {
        ProductResponse updated = productService.updateProduct(id, request);
        return ResponseEntity.ok(updated);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProduct(@PathVariable("id") Long id) {
        ProductResponse product = productService.getProductById(id);
        return ResponseEntity.ok(product);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable("id") Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}