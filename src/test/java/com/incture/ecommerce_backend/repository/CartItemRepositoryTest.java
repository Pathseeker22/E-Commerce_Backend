package com.incture.ecommerce_backend.repository;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.incture.ecommerce_backend.entity.CartItemEntity;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CartItemRepositoryTest {

    @Autowired
    private CartItemRepository cartItemRepository;

    @Test
    void whenSaveCartItem_thenSuccess() {
        CartItemEntity item = new CartItemEntity();
        item.setQuantity(5);

        CartItemEntity saved = cartItemRepository.save(item);
        assertNotNull(saved.getId());
        assertEquals(5, saved.getQuantity());
    }
}
