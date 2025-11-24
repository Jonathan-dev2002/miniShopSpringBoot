package com.miniProject.miniShop.dto;

import com.miniProject.miniShop.model.OrderStatus;
import lombok.Data;

@Data
public class OrderSearchRequest {
    private int page = 0;
    private int size = 10;
    private String keyword;
    private OrderStatus status;
}