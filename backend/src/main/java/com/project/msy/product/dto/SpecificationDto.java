package com.project.msy.product.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SpecificationDto {
    private String label;
    private String value;
}