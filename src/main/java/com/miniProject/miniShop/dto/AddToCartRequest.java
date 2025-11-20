package com.miniProject.miniShop.dto;
import lombok.Data;

import java.util.UUID;

@Data
public class AddToCartRequest {
    private UUID productId;
    private Integer quantity;
}