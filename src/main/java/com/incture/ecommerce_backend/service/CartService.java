package com.incture.ecommerce_backend.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.incture.ecommerce_backend.entity.CartEntity;
import com.incture.ecommerce_backend.entity.CartItemEntity;
import com.incture.ecommerce_backend.entity.ProductEntity;
import com.incture.ecommerce_backend.entity.UserEntity;
import com.incture.ecommerce_backend.exception.ResourceNotFoundException;
import com.incture.ecommerce_backend.repository.CartItemRepository;
import com.incture.ecommerce_backend.repository.CartRepository;
import com.incture.ecommerce_backend.repository.ProductRepository;
import com.incture.ecommerce_backend.repository.UserRepository;

/**
 * Service class for handling shopping cart business operations.
 */

@Service
public class CartService {

	private static final Logger logger = LoggerFactory.getLogger(CartService.class);

	@Autowired
	private CartRepository cartRepository;
	
	@Autowired
	private CartItemRepository cartItemRepository;
	
	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private UserRepository userRepository;
	
	// Adds a product to the user's cart
	public CartItemEntity addProductToCart(String userEmail, Long productId, Integer quantity) {
		logger.info("Adding product ID: {} to cart for user: {}", productId, userEmail);
		validateQuantity(quantity);
		CartEntity cartEntity = getOrCreateCart(userEmail);
		ProductEntity productEntity = getProductById(productId);
		
		// Check if product already exists in cart
		CartItemEntity existingCartItem = cartItemRepository.findByCartEntityAndProductEntity(cartEntity, productEntity);
		
		if(existingCartItem != null) {
			int updatedQuantity = existingCartItem.getQuantity() + quantity;
			validateStock(productEntity, updatedQuantity);
			
			// Update quantity
			existingCartItem.setQuantity(updatedQuantity);
			
			CartItemEntity savedCartItem = cartItemRepository.save(existingCartItem);
			refreshCartTotal(cartEntity);
			return savedCartItem;
		}

		validateStock(productEntity, quantity);
		
		CartItemEntity cartItemEntity = new CartItemEntity();
		cartItemEntity.setCartEntity(cartEntity);
		cartItemEntity.setProductEntity(productEntity);
		cartItemEntity.setQuantity(quantity);
		
		CartItemEntity savedCartItem = cartItemRepository.save(cartItemEntity);
		refreshCartTotal(cartEntity);
		return savedCartItem;
	}
	
	// Update quantity of a cart item
	public CartItemEntity updateCartItem(String userEmail, Long productId, Integer quantity) {
		logger.info("Updating product ID: {} quantity to {} for user: {}", productId, quantity, userEmail);
		validateQuantity(quantity);
		CartEntity cartEntity = getOrCreateCart(userEmail);
		ProductEntity productEntity = getProductById(productId);
		CartItemEntity cartItemEntity = cartItemRepository.findByCartEntityAndProductEntity(cartEntity, productEntity);
		if (cartItemEntity == null) {
			logger.warn("Update cart failed: Product ID {} not found in cart for user {}", productId, userEmail);
			throw new ResourceNotFoundException("Cart item not found");
		}
		validateStock(productEntity, quantity);
		cartItemEntity.setQuantity(quantity);
		CartItemEntity savedCartItem = cartItemRepository.save(cartItemEntity);
		refreshCartTotal(cartEntity);
		return savedCartItem;
	}
	
	// Remove product from cart
	public void removeProductFromCart(String userEmail, Long productId) {
		logger.info("Removing product ID: {} from cart for user: {}", productId, userEmail);
		CartEntity cartEntity = getOrCreateCart(userEmail);
		ProductEntity productEntity = getProductById(productId);
		CartItemEntity cartItemEntity = cartItemRepository.findByCartEntityAndProductEntity(cartEntity, productEntity);
		if (cartItemEntity == null) {
			throw new ResourceNotFoundException("Cart item not found");
		}
		cartItemRepository.delete(cartItemEntity);
		refreshCartTotal(cartEntity);
	}
	
	// View current user's cart
	public CartEntity getCartByUser(String userEmail){
		CartEntity cartEntity = getOrCreateCart(userEmail);
		refreshCartTotal(cartEntity);
		return cartEntity;
	}

	private CartEntity getOrCreateCart(String userEmail) {
		UserEntity userEntity = getUserByEmail(userEmail);
		CartEntity cartEntity = cartRepository.findByUserEntity(userEntity);
		if (cartEntity != null) {
			if (cartEntity.getCartItemEntities() == null) {
				cartEntity.setCartItemEntities(new ArrayList<>());
			}
			return cartEntity;
		}

		CartEntity newCartEntity = new CartEntity();
		newCartEntity.setUserEntity(userEntity);
		newCartEntity.setTotalPrice(0.0);
		newCartEntity.setCartItemEntities(new ArrayList<>());
		return cartRepository.save(newCartEntity);
	}

	private UserEntity getUserByEmail(String email) {
		UserEntity userEntity = userRepository.findByEmail(email);
		if (userEntity == null) {
			throw new ResourceNotFoundException("User not found");
		}
		return userEntity;
	}

	private ProductEntity getProductById(Long productId) {
		return productRepository.findById(productId)
				.orElseThrow(() -> new ResourceNotFoundException("Product not found"));
	}

	private void refreshCartTotal(CartEntity cartEntity) {
		List<CartItemEntity> cartItems = cartItemRepository.findAllByCartEntity(cartEntity);
		cartEntity.setCartItemEntities(cartItems);
		double totalPrice = 0.0;
		for (CartItemEntity cartItemEntity : cartItems) {
			totalPrice += cartItemEntity.getProductEntity().getPrice() * cartItemEntity.getQuantity();
		}
		cartEntity.setTotalPrice(totalPrice);
		cartRepository.save(cartEntity);
	}

	private void validateQuantity(Integer quantity) {
		if (quantity == null || quantity <= 0) {
			throw new IllegalArgumentException("Quantity must be greater than zero");
		}
	}

	private void validateStock(ProductEntity productEntity, int quantity) {
		if (productEntity.getStock() == null || productEntity.getStock() < quantity) {
			logger.warn("Stock validation failed for product {}: requested {}, available {}", 
					productEntity.getName(), quantity, productEntity.getStock());
			throw new RuntimeException("Requested quantity exceeds available stock");
		}
	}
}
