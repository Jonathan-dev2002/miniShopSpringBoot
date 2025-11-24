package com.miniProject.miniShop.dto;

import lombok.Data;
import java.util.UUID;

@Data
public class ProductSearchRequest {
    private int page = 0;
    private int size = 10;
    private String keyword;
    private UUID categoryId;
    private Double minPrice;
    private Double maxPrice;
}