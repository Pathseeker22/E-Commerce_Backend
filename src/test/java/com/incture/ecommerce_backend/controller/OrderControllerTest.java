package com.incture.ecommerce_backend.controller;

import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.incture.ecommerce_backend.entity.OrderEntity;
import com.incture.ecommerce_backend.service.OrderService;

/**
 * Integration tests for Order API endpoints.
 * Uses MockBean to isolate controller testing.
 */
@SpringBootTest
@AutoConfigureMockMvc
public class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    @Test
    @WithMockUser(roles = "CUSTOMER", username = "user@example.com")
    void whenGetOrdersAsCustomer_thenOk() throws Exception {
        when(orderService.getOrders(anyString(), anyBoolean())).thenReturn(new ArrayList<>());
        mockMvc.perform(get("/api/orders"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "CUSTOMER", username = "user@example.com")
    void whenCheckoutAsCustomer_thenOk() throws Exception {
        when(orderService.checkout(anyString(), anyString())).thenReturn(new OrderEntity());
        mockMvc.perform(post("/api/orders/checkout")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"paymentMode\": \"COD\"}"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ADMIN", username = "admin@example.com")
    void whenGetOrderByIdAsAdmin_thenOk() throws Exception {
        when(orderService.getOrderById(anyLong(), anyString(), anyBoolean())).thenReturn(new OrderEntity());
        mockMvc.perform(get("/api/orders/1"))
                .andExpect(status().isOk());
    }
}
