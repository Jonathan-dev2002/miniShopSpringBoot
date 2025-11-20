package com.miniProject.miniShop.service;

import com.miniProject.miniShop.dto.AddressDto;
import com.miniProject.miniShop.dto.CreateOrderRequest;
import com.miniProject.miniShop.model.*;
import com.miniProject.miniShop.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final UserRepository userRepository;
    private final AddressRepository addressRepository;
    private final ProductRepository productRepository;

    @Transactional
    public Order createOrder(String email, CreateOrderRequest request) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));

        // 1. เตรียมที่อยู่จัดส่ง (Logic เหมือนใน JS Controller)
        String shippingAddressStr;
        String phoneStr;

        if (request.getSelectedAddressId() != null) {
            Address address = addressRepository.findByIdAndUserId(request.getSelectedAddressId(), user.getId())
                    .orElseThrow(() -> new RuntimeException("Address not found"));
            shippingAddressStr = address.getRecipientName() + ", " + address.getStreet() + ", " + address.getCity() + " " + address.getPostalCode();
            phoneStr = address.getPhone();
        } else if (request.getNewShippingAddress() != null) {
            AddressDto newAddr = request.getNewShippingAddress();
            shippingAddressStr = newAddr.getRecipientName() + ", " + newAddr.getStreet() + ", " + newAddr.getCity() + " " + newAddr.getPostalCode();
            phoneStr = newAddr.getPhone();
        } else {
            throw new RuntimeException("Shipping address is required");
        }

        // ดึงตะกร้าสินค้า
        Cart cart = cartRepository.findByUser(user).orElseThrow(() -> new RuntimeException("Cart is empty"));
        List<CartItem> cartItems = cartItemRepository.findByCartId(cart.getId());

        if (cartItems.isEmpty()) {
            throw new RuntimeException("Cart is empty");
        }

        // คำนวณราคา และเช็คสต็อก
        double totalAmount = 0;
        for (CartItem item : cartItems) {
            if (item.getQuantity() > item.getProduct().getStock()) {
                throw new RuntimeException("Insufficient stock for product: " + item.getProduct().getName());
            }
            totalAmount += item.getQuantity() * item.getProduct().getPrice();
        }

        // Order
        Order order = new Order();
        order.setUser(user);
        order.setTotalAmount(totalAmount);
        order.setPaymentMethod(request.getPaymentMethod());
        order.setShippingAddress(shippingAddressStr);
        order.setPhone(phoneStr);
        order.setStatus(request.getPaymentMethod() == PaymentMethod.COD ? OrderStatus.CONFIRMED : OrderStatus.PENDING);

        Order savedOrder = orderRepository.save(order);

        // OrderItems และตัดสต็อก
        for (CartItem cartItem : cartItems) {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(savedOrder);
            orderItem.setProduct(cartItem.getProduct());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setUnitPrice(cartItem.getProduct().getPrice());
            orderItemRepository.save(orderItem);

            // ตัดสต็อก
            Product product = cartItem.getProduct();
            product.setStock(product.getStock() - cartItem.getQuantity());
            productRepository.save(product);
        }

        // 6. ล้างตะกร้า
        cartItemRepository.deleteAll(cartItems); // หรือ deleteByCartId

        return savedOrder;
    }

    public List<Order> getMyOrders(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
        return orderRepository.findByUserIdOrderByCreatedAtDesc(user.getId());
    }

    public Order getOrderById(UUID id, String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
        return orderRepository.findByIdAndUserId(id, user.getId())
                .orElseThrow(() -> new RuntimeException("Order not found"));
    }

    @Transactional
    public Order updateOrderStatus(UUID id, OrderStatus newStatus) {
        Order order = orderRepository.findById(id).orElseThrow(() -> new RuntimeException("Order not found"));

        //คืนสต็อกสินค้า
        if (newStatus == OrderStatus.CANCELLED && order.getStatus() != OrderStatus.CANCELLED) {
            for (OrderItem item : order.getItems()) {
                Product product = item.getProduct();
                product.setStock(product.getStock() + item.getQuantity());
                productRepository.save(product);
            }
        }

        order.setStatus(newStatus);
        return orderRepository.save(order);
    }
}