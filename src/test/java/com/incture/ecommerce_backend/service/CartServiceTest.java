package com.incture.ecommerce_backend.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.incture.ecommerce_backend.entity.CartEntity;
import com.incture.ecommerce_backend.entity.UserEntity;
import com.incture.ecommerce_backend.repository.CartItemRepository;
import com.incture.ecommerce_backend.repository.CartRepository;
import com.incture.ecommerce_backend.repository.ProductRepository;
import com.incture.ecommerce_backend.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
public class CartServiceTest {

    @Mock
    private CartRepository cartRepository;
    
    @Mock
    private CartItemRepository cartItemRepository;
    
    @Mock
    private ProductRepository productRepository;
    
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CartService cartService;

    @Test
    void whenGetCartByUser_thenReturnsCart() {
        String email = "customer@example.com";
        UserEntity user = new UserEntity();
        user.setEmail(email);

        CartEntity cart = new CartEntity();
        cart.setUserEntity(user);
        cart.setCartItemEntities(new ArrayList<>());
        cart.setTotalPrice(0.0);

        when(userRepository.findByEmail(email)).thenReturn(user);
        when(cartRepository.findByUserEntity(user)).thenReturn(cart);

        CartEntity result = cartService.getCartByUser(email);

        assertNotNull(result);
        assertEquals(0.0, result.getTotalPrice());
        verify(cartRepository, times(1)).findByUserEntity(user);
    }
}
