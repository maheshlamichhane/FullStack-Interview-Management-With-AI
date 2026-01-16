package com.common.project.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductCreatedEventDTO {
    String productId;
    private String title;
    private BigDecimal price;
    private Integer quantity;
}
