package com.miniProject.miniShop.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper = true)
public class ProductSearchRequest extends BaseSearchRequest{
    private UUID categoryId;
    private Double minPrice;
    private Double maxPrice;
}