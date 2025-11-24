package com.miniProject.miniShop.dto;

import lombok.Data;

@Data
public class CartSearchRequest {
    private int page = 0;
    private int size = 10;
    private String keyword;
}