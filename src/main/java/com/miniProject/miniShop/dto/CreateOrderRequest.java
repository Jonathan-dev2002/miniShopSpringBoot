package com.miniProject.miniShop.dto;
import com.miniProject.miniShop.model.PaymentMethod;
import lombok.Data;

import java.util.UUID;

@Data
public class CreateOrderRequest {
    private PaymentMethod paymentMethod;
    private UUID selectedAddressId;
    private AddressDto newShippingAddress; // ใช้ AddressDto เดิมที่มีอยู่แล้ว
}