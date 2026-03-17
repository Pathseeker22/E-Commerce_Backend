package com.incture.ecommerce_backend.controller;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.incture.ecommerce_backend.entity.CartEntity;
import com.incture.ecommerce_backend.entity.CartItemEntity;
import com.incture.ecommerce_backend.service.CartService;

/**
 * Integration tests for Shopping Cart API.
 * Uses MockBean to isolate controller testing.
 */
@SpringBootTest
@AutoConfigureMockMvc
public class CartControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CartService cartService;

    @Test
    @WithMockUser(roles = "CUSTOMER", username = "user@example.com")
    void whenViewCart_thenOk() throws Exception {
        when(cartService.getCartByUser(anyString())).thenReturn(new CartEntity());

        mockMvc.perform(get("/api/cart"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "CUSTOMER", username = "user@example.com")
    void whenAddToCart_thenOk() throws Exception {
        when(cartService.addProductToCart(anyString(), anyLong(), anyInt())).thenReturn(new CartItemEntity());

        mockMvc.perform(post("/api/cart/add/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"quantity\": 2}"))
                .andExpect(status().isOk());
    }
}
