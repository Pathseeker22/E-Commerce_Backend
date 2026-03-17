package com.incture.ecommerce_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.incture.ecommerce_backend.entity.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    UserEntity findByEmail(String email);

}
