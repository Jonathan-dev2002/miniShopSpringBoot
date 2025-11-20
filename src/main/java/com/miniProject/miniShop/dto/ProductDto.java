package com.miniProject.miniShop.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class ProductDto {
    private String name;
    private String description;
    private Double price;
    private Integer stock;
    private UUID categoryId;
}