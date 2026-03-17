package com.incture.ecommerce_backend.repository;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.incture.ecommerce_backend.entity.CartEntity;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CartRepositoryTest {

    @Autowired
    private CartRepository cartRepository;

    @Test
    void whenSaveCart_thenSuccess() {
        CartEntity cart = new CartEntity();
        cart.setTotalPrice(0.0);

        CartEntity saved = cartRepository.save(cart);
        assertNotNull(saved.getId());
    }
}
