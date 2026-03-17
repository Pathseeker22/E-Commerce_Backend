package com.incture.ecommerce_backend.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.incture.ecommerce_backend.entity.ProductEntity;

public interface ProductRepository extends JpaRepository<ProductEntity, Long>{

    Page<ProductEntity> findByCategory(String category, Pageable pageable);

    Page<ProductEntity> findByPriceBetween(Double minPrice, Double maxPrice, Pageable pageable);

    Page<ProductEntity> findByCategoryAndPriceBetween(String category, Double minPrice, Double maxPrice, Pageable pageable);

}
