package com.incture.ecommerce_backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.incture.ecommerce_backend.entity.OrderEntity;
import com.incture.ecommerce_backend.entity.UserEntity;

public interface OrderRepository extends JpaRepository<OrderEntity, Long> {

    List<OrderEntity> findByUserEntity(UserEntity userEntity);
}
