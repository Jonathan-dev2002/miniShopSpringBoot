package com.miniProject.miniShop.config;

public class SecurityConstants {
    // รวม Path ที่อนุญาตให้เข้าถึงได้โดยไม่ต้อง Login
    public static final String[] PUBLIC_URLS = {
            "/api/auth/login",
            "/api/auth/register",
            "/api/products",
            "/api/products/**",    // อนุญาตให้ดูรายละเอียดสินค้า (GetById)
            "/api/categories/**"   // อนุญาตให้ดูหมวดหมู่
    };
}
