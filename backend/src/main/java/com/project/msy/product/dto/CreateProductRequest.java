package com.project.msy.product.dto;

import lombok.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateProductRequest {
    @NotBlank
    private String name;

    @NotNull
    private Long category;

    @NotNull
    private BigDecimal price;

    private BigDecimal discountPrice;

    @NotNull
    @Min(0)
    private Integer stock;

    private String description;

    private BigDecimal shippingFee;

    private String manufacturer;

    @NotNull
    private Long origin;

    private List<SpecificationDto> specifications;

    private List<String> features;

}
