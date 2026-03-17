package com.incture.ecommerce_backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.incture.ecommerce_backend.entity.ProductEntity;
import com.incture.ecommerce_backend.service.ProductService;

/**
 * Controller for managing the product catalog.
 */
@RestController
@RequestMapping(path = "/api/products")
public class ProductController {
	
	@Autowired
	private ProductService productService;
	
	// Add Product
	@PostMapping
	@PreAuthorize("hasRole('ADMIN')")
	public ProductEntity addProduct(@RequestBody ProductEntity productEntity) {
		return productService.addProduct(productEntity);
	}
	
	// Get Products with optional filtering
	@GetMapping
	@PreAuthorize("isAuthenticated()")
	public Page<ProductEntity> getAllProducts(@RequestParam(name = "category", required = false) String category,
											  @RequestParam(name = "minPrice", required = false) Double minPrice,
											  @RequestParam(name = "maxPrice", required = false) Double maxPrice,
											  Pageable pageable) {
	    return productService.getAllProducts(category, minPrice, maxPrice, pageable);
	}
	
	// Get Product by Id
	@GetMapping(value = "/{id}")
	@PreAuthorize("isAuthenticated()")
	 public ProductEntity getProductById(@PathVariable(name = "id") Long id) {
		return productService.getProductById(id);
	}
	
	// Update Product
	@PutMapping(value = "/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ProductEntity updateProduct(@PathVariable(name = "id") Long id, @RequestBody ProductEntity productEntity) {
		return productService.updateProduct(id, productEntity);
	}
	
	// Delete product by Id
	@DeleteMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public String deleteProduct(@PathVariable(name = "id") Long id) {
		productService.deleteProduct(id);
		
		return "Product deleted successfully";
	}
}
