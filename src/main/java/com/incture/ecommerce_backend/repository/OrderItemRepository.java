package com.incture.ecommerce_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.incture.ecommerce_backend.entity.OrderItemEntity;

public interface OrderItemRepository extends JpaRepository<OrderItemEntity, Long> {

}
