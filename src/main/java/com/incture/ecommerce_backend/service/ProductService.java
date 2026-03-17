package com.incture.ecommerce_backend.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.incture.ecommerce_backend.entity.ProductEntity;
import com.incture.ecommerce_backend.exception.ResourceNotFoundException;
import com.incture.ecommerce_backend.repository.ProductRepository;

/**
 * Service class for product management and catalog discovery.
 */

@Service
public class ProductService {

	private static final Logger logger = LoggerFactory.getLogger(ProductService.class);

	@Autowired
	private ProductRepository productRepository;
	
	// Admin creates new product
	public ProductEntity addProduct(ProductEntity productEntity) {
		logger.info("Admin adding new product: {}", productEntity.getName());
		return productRepository.save(productEntity);
	}
	
	// Customer views product catalog with optional filtering
	public Page<ProductEntity> getAllProducts(String category, Double minPrice, Double maxPrice, Pageable pageable) {
		logger.info("Fetching products with category: {}, price range: {} - {}", category, minPrice, maxPrice);
		if (category != null && minPrice != null && maxPrice != null) {
			return productRepository.findByCategoryAndPriceBetween(category, minPrice, maxPrice, pageable);
		} else if (category != null) {
			return productRepository.findByCategory(category, pageable);
		} else if (minPrice != null && maxPrice != null) {
			return productRepository.findByPriceBetween(minPrice, maxPrice, pageable);
		}
	    return productRepository.findAll(pageable);
	}
	
	// Get product by Id
	public ProductEntity getProductById(Long id) {
		return productRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Product not found"));
	}
	
	// Admin updates the existing product
	public ProductEntity updateProduct(Long id, ProductEntity productEntity) {
		logger.info("Admin updating product ID: {}", id);
		ProductEntity existingProduct = getProductById(id);
		
		if(existingProduct != null) {
			existingProduct.setName(productEntity.getName());
			existingProduct.setDescription(productEntity.getDescription());
			existingProduct.setPrice(productEntity.getPrice());
			existingProduct.setStock(productEntity.getStock());
			existingProduct.setCategory(productEntity.getCategory());
			existingProduct.setImageUrl(productEntity.getImageUrl());
			existingProduct.setRating(productEntity.getRating());
		
			return productRepository.save(existingProduct);
		}
		return null;
	}
	
	// Admin deletes the product by Id
	public void deleteProduct(Long id) {
		logger.warn("Admin deleting product ID: {}", id);
		productRepository.delete(getProductById(id));
	}
	
}
