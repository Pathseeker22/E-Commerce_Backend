package com.incture.ecommerce_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.incture.ecommerce_backend.entity.CartItemEntity;
import java.util.List;
import com.incture.ecommerce_backend.entity.ProductEntity;
import com.incture.ecommerce_backend.entity.CartEntity;


public interface CartItemRepository extends JpaRepository<CartItemEntity, Long> {
	CartItemEntity  findByCartEntityAndProductEntity(CartEntity cartEntity, ProductEntity productEntity);
	List<CartItemEntity> findAllByCartEntity(CartEntity cartEntity);
}
