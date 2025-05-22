package com.project.msy.admin.dashboard.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PopularProductDto {
    private Long      id;
    private String    name;
    private Long      salesCount;
    private BigDecimal revenue;
    private Integer   stockQuantity;
    private String  categoryName;

    public PopularProductDto(long l, String s, long l1, BigDecimal bigDecimal, int i, String s1, Object o) {
    }
}