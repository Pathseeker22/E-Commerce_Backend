package com.incture.ecommerce_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.incture.ecommerce_backend.entity.CartEntity;
import com.incture.ecommerce_backend.entity.UserEntity;

public interface CartRepository extends JpaRepository<CartEntity, Long>{

    CartEntity findByUserEntity(UserEntity userEntity);
}
