package com.incture.ecommerce_backend.repository;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.incture.ecommerce_backend.entity.OrderEntity;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class OrdersRepositoryTest {

    @Autowired
    private OrderRepository orderRepository;

    @Test
    void whenSaveOrder_thenSuccess() {
        OrderEntity order = new OrderEntity();
        order.setTotalAmount(450.0);
        order.setOrderStatus("PROCESSING");

        OrderEntity saved = orderRepository.save(order);
        assertNotNull(saved.getId());
        assertEquals(450.0, saved.getTotalAmount());
    }
}
