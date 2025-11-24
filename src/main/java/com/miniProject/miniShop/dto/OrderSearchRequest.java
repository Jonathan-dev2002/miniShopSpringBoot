package com.miniProject.miniShop.dto;

import com.miniProject.miniShop.model.OrderStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class OrderSearchRequest extends BaseSearchRequest{
    private OrderStatus status;
}