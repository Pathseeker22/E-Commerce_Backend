package com.incture.ecommerce_backend.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.incture.ecommerce_backend.entity.CartEntity;
import com.incture.ecommerce_backend.entity.CartItemEntity;
import com.incture.ecommerce_backend.entity.OrderEntity;
import com.incture.ecommerce_backend.entity.ProductEntity;
import com.incture.ecommerce_backend.entity.UserEntity;
import com.incture.ecommerce_backend.repository.CartItemRepository;
import com.incture.ecommerce_backend.repository.CartRepository;
import com.incture.ecommerce_backend.repository.OrderItemRepository;
import com.incture.ecommerce_backend.repository.OrderRepository;
import com.incture.ecommerce_backend.repository.ProductRepository;
import com.incture.ecommerce_backend.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
public class OrdersServiceTest {

    @Mock
    private OrderRepository orderRepository;
    @Mock
    private OrderItemRepository orderItemRepository;
    @Mock
    private CartItemRepository cartItemRepository;
    @Mock
    private CartRepository cartRepository;
    @Mock
    private ProductRepository productRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private EmailService emailService;

    @InjectMocks
    private OrderService orderService;

    @Test
    void whenCheckout_thenCreateOrder() {
        String email = "buyer@example.com";
        UserEntity user = new UserEntity();
        user.setEmail(email);
        user.setName("John Buyer");

        ProductEntity product = new ProductEntity();
        product.setPrice(50.0);
        product.setStock(10);

        CartItemEntity cartItem = new CartItemEntity();
        cartItem.setProductEntity(product);
        cartItem.setQuantity(2);

        CartEntity cart = new CartEntity();
        cart.setUserEntity(user);
        cart.setCartItemEntities(List.of(cartItem));

        when(userRepository.findByEmail(email)).thenReturn(user);
        when(cartRepository.findByUserEntity(user)).thenReturn(cart);
        when(orderRepository.save(any(OrderEntity.class))).thenAnswer(i -> i.getArguments()[0]);

        OrderEntity order = orderService.checkout(email, "CREDIT_CARD");

        assertNotNull(order);
        assertEquals(100.0, order.getTotalAmount());
        assertEquals("CREDIT_CARD", order.getPaymentMode());
        verify(emailService, times(1)).sendOrderConfirmation(any());
        verify(productRepository, times(1)).save(any());
    }
}
