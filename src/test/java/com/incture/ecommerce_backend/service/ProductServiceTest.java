package com.incture.ecommerce_backend.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.incture.ecommerce_backend.entity.ProductEntity;
import com.incture.ecommerce_backend.repository.ProductRepository;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    @Test
    void whenAddProduct_thenSucceed() {
        ProductEntity product = new ProductEntity();
        product.setName("Laptop");
        product.setPrice(1200.0);

        when(productRepository.save(any(ProductEntity.class))).thenReturn(product);

        ProductEntity savedProduct = productService.addProduct(product);

        assertNotNull(savedProduct);
        assertEquals("Laptop", savedProduct.getName());
        verify(productRepository, times(1)).save(product);
    }

    @Test
    void whenGetProductById_thenReturnsProduct() {
        ProductEntity product = new ProductEntity();
        product.setId(1L);
        product.setName("Mouse");

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        ProductEntity result = productService.getProductById(1L);

        assertNotNull(result);
        assertEquals("Mouse", result.getName());
    }
}
