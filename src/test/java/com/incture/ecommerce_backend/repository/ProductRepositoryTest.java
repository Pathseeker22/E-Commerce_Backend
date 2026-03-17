package com.incture.ecommerce_backend.repository;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.incture.ecommerce_backend.entity.ProductEntity;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Test
    void whenSaveProduct_thenSuccess() {
        ProductEntity product = new ProductEntity();
        product.setName("Coffee Maker");
        product.setPrice(89.99);
        product.setStock(10);
        product.setCategory("Kitchen");

        ProductEntity saved = productRepository.save(product);
        assertNotNull(saved.getId());
        assertEquals("Coffee Maker", saved.getName());
    }
}
