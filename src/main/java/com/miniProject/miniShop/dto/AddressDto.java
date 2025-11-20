package com.miniProject.miniShop.dto;

import lombok.Data;

@Data
public class AddressDto {
    private String recipientName;
    private String phone;
    private String street;
    private String city;
    private String province;
    private String postalCode;
    private String country = "Thailand";
    private Boolean isDefault;
    private String label;
}