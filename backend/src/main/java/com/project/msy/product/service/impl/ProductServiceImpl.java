package com.project.msy.product.service.impl;

import com.project.msy.product.dto.CreateProductRequest;
import com.project.msy.product.dto.ProductResponse;
import com.project.msy.product.dto.SpecificationDto;
import com.project.msy.product.dto.UpdateProductRequest;
import com.project.msy.product.entity.*;
import com.project.msy.product.repository.ProductCategoryRepository;
import com.project.msy.product.repository.ProductImageRepository;
import com.project.msy.product.repository.ProductOriginRepository;
import com.project.msy.product.repository.ProductRepository;
import com.project.msy.product.service.ProductService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final ProductImageRepository productImageRepository;
    private final ProductCategoryRepository categoryRepository;
    private final ProductOriginRepository originRepository;

    @Override
    @Transactional
    public ProductResponse createProduct(CreateProductRequest request) {
        ProductCategory category = categoryRepository.findById(request.getCategory())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.BAD_REQUEST, "유효하지 않은 카테고리: " + request.getCategory()));
        ProductOrigin origin = originRepository.findById(request.getOrigin())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.BAD_REQUEST, "유효하지 않은 원산지: " + request.getOrigin()));

        Product product = Product.builder()
                .name(request.getName())
                .category(category)
                .price(request.getPrice())
                .discountPrice(request.getDiscountPrice())
                .stockQuantity(request.getStock())
                .description(request.getDescription())
                .shippingFee(request.getShippingFee())
                .manufacturer(request.getManufacturer())
                .origin(origin)
                .build();

        // 사양 저장
        if (request.getSpecifications() != null) {
            request.getSpecifications().forEach(dto -> {
                ProductSpecification spec = ProductSpecification.builder()
                        .product(product)
                        .specName(dto.getLabel())
                        .specValue(dto.getValue())
                        .build();
                product.getSpecifications().add(spec);
            });
        }

        // 상품 특징 저장
        if (request.getFeatures() != null) {
            request.getFeatures().forEach(name -> {
                ProductFeature feature = ProductFeature.builder()
                        .product(product)
                        .feature(name)
                        .build();
                product.getFeatures().add(feature);
            });
        }

        Product saved = productRepository.save(product);
        return buildResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductResponse> getAllProducts() {
        return productRepository.findAll().stream()
                .map(this::buildResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public ProductResponse getProductById(Long productId) {
        Product p = productRepository.findById(productId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "상품을 찾을 수 없습니다: " + productId));
        return buildResponse(p);
    }

    @Override
    @Transactional
    public ProductResponse updateProduct(Long productId, UpdateProductRequest request) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "상품을 찾을 수 없습니다: " + productId));

        ProductCategory category = categoryRepository.findById(request.getCategory())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.BAD_REQUEST, "유효하지 않은 카테고리: " + request.getCategory()));
        ProductOrigin origin = originRepository.findById(request.getOrigin())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.BAD_REQUEST, "유효하지 않은 원산지: " + request.getOrigin()));

        // 기본 필드 업데이트
        product.setName(request.getName());
        product.setCategory(category);
        product.setPrice(request.getPrice());
        product.setDiscountPrice(request.getDiscountPrice());
        product.setStockQuantity(request.getStock());
        product.setDescription(request.getDescription());
        product.setShippingFee(request.getShippingFee());
        product.setManufacturer(request.getManufacturer());
        product.setOrigin(origin);

        // 사양 업데이트
        product.getSpecifications().clear();
        if (request.getSpecifications() != null) {
            request.getSpecifications().forEach(dto -> {
                ProductSpecification spec = ProductSpecification.builder()
                        .product(product)
                        .specName(dto.getLabel())
                        .specValue(dto.getValue())
                        .build();
                product.getSpecifications().add(spec);
            });
        }

        // 상품 특징 업데이트
        product.getFeatures().clear();
        if (request.getFeatures() != null) {
            request.getFeatures().forEach(name -> {
                ProductFeature feature = ProductFeature.builder()
                        .product(product)
                        .feature(name)
                        .build();
                product.getFeatures().add(feature);
            });
        }

        Product updated = productRepository.save(product);
        return buildResponse(updated);
    }

    @Override
    @Transactional
    public void deleteProduct(Long productId) {
        if (!productRepository.existsById(productId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "상품을 찾을 수 없습니다: " + productId);
        }
        productRepository.deleteById(productId);
    }

    private ProductResponse buildResponse(Product p) {
        return ProductResponse.builder()
                .id(p.getId())
                .name(p.getName())
                .category(p.getCategory().getId())
                .categoryName(p.getCategory().getName())
                .price(p.getPrice())
                .discountPrice(p.getDiscountPrice())
                .stockQuantity(p.getStockQuantity())
                .description(p.getDescription())
                .shippingFee(p.getShippingFee())
                .manufacturer(p.getManufacturer())
                .origin(p.getOrigin().getId())
                .originName(p.getOrigin().getName())
                .createdAt(p.getCreatedAt())
                .specifications(p.getSpecifications().stream()
                        .map(s -> new SpecificationDto(s.getSpecName(), s.getSpecValue()))
                        .collect(Collectors.toList()))
                .features(p.getFeatures().stream()
                        .map(ProductFeature::getFeature)
                        .collect(Collectors.toList()))
                .images(
                        p.getImages().stream()
                                .map(ProductImage::getImageUrl)
                                .collect(Collectors.toList())
                )
                .build();
    }

    @Override
    @Transactional
    public void addImage(Long productId, String imageUrl) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 상품 id=" + productId));

        ProductImage img = ProductImage.builder()
                .product(product)
                .imageUrl(imageUrl)
                .isMain(false)  // 필요에 따라 true/false 설정
                .build();

        product.getImages().add(img);
        productImageRepository.save(img);
    }
}