package com.miniProject.miniShop.dto;
import com.miniProject.miniShop.model.OrderStatus;
import lombok.Data;

@Data
public class UpdateOrderStatusRequest {
    private OrderStatus status;
}