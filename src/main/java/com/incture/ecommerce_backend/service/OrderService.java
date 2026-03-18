package com.incture.ecommerce_backend.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import com.incture.ecommerce_backend.entity.CartEntity;
import com.incture.ecommerce_backend.entity.CartItemEntity;
import com.incture.ecommerce_backend.entity.OrderEntity;
import com.incture.ecommerce_backend.entity.OrderItemEntity;
import com.incture.ecommerce_backend.entity.ProductEntity;
import com.incture.ecommerce_backend.entity.UserEntity;
import com.incture.ecommerce_backend.exception.ResourceNotFoundException;
import com.incture.ecommerce_backend.repository.CartItemRepository;
import com.incture.ecommerce_backend.repository.CartRepository;
import com.incture.ecommerce_backend.repository.OrderItemRepository;
import com.incture.ecommerce_backend.repository.OrderRepository;
import com.incture.ecommerce_backend.repository.ProductRepository;
import com.incture.ecommerce_backend.repository.UserRepository;

/**
 * Service class for handling order placement and history.
 */

@Service
public class OrderService {
	
	private static final Logger logger = LoggerFactory.getLogger(OrderService.class);
	
	@Autowired
	private OrderRepository orderRepository;
	
	@Autowired
	private OrderItemRepository orderItemRepository;
	
	@Autowired
	private CartItemRepository cartItemRepository;
	
	@Autowired
	private CartRepository cartRepository;

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private EmailService emailService;
	
	public OrderEntity checkout(String userEmail, String paymentMode) {
		UserEntity userEntity = getUserByEmail(userEmail);
		CartEntity cartEntity = cartRepository.findByUserEntity(userEntity);
		if (cartEntity == null || cartEntity.getCartItemEntities() == null || cartEntity.getCartItemEntities().isEmpty()) {
			logger.warn("Checkout attempt for user {} with empty cart", userEmail);
			throw new IllegalArgumentException("Cart is empty");
		}
		
		logger.info("Initiating checkout for user: {}", userEmail);
		List<CartItemEntity> cartItemEntities = cartEntity.getCartItemEntities();
		
		OrderEntity orderEntity = new OrderEntity();
		orderEntity.setUserEntity(userEntity);
		orderEntity.setOrderDate(LocalDateTime.now());
		orderEntity.setPaymentMode(paymentMode != null ? paymentMode.trim().toUpperCase() : "COD");
		
		// Simulation of payment success/failure
		boolean paymentSuccessful = !orderEntity.getPaymentMode().startsWith("FAIL");
		
		if (paymentSuccessful) {
			orderEntity.setPaymentStatus("SUCCESS");
			orderEntity.setOrderStatus("PLACED");
			logger.info("Payment successful for user: {}", userEmail);
		} else {
			orderEntity.setPaymentStatus("FAILED");
			orderEntity.setOrderStatus("CANCELLED");
			logger.warn("Payment failed simulation for user: {}", userEmail);
		}
		
		orderEntity = orderRepository.save(orderEntity);
		
		List<OrderItemEntity> orderItemEntities = new ArrayList<>();
		double totalAmount = 0;
		
		for(CartItemEntity cartItemEntity : cartItemEntities) {
			ProductEntity productEntity = cartItemEntity.getProductEntity();
			
			if(paymentSuccessful) {
				if(productEntity.getStock() < cartItemEntity.getQuantity()) {
					throw new RuntimeException("Product " + productEntity.getName() + " out of stock");
				}
				productEntity.setStock(productEntity.getStock() - cartItemEntity.getQuantity());
				productRepository.save(productEntity);
			}
			
			OrderItemEntity orderItemEntity = new OrderItemEntity();
			orderItemEntity.setOrderEntity(orderEntity);
			orderItemEntity.setProductEntity(productEntity);
			orderItemEntity.setQuantity(cartItemEntity.getQuantity());
			orderItemEntity.setPrice(productEntity.getPrice());
			
			totalAmount += productEntity.getPrice() * cartItemEntity.getQuantity();
			orderItemEntities.add(orderItemEntity);
		}
		
		orderItemRepository.saveAll(orderItemEntities);
		
		orderEntity.setTotalAmount(totalAmount);
		orderEntity.setOrderItemEntities(orderItemEntities);
		
		// Only clear cart if checkout/payment was successful
		if (paymentSuccessful) {
			cartItemRepository.deleteAll(cartItemEntities);
			cartEntity.setTotalPrice(0.0);
			cartRepository.save(cartEntity);
		}
		
		OrderEntity savedOrder = orderRepository.save(orderEntity);
		
		// Send email notification
		if (paymentSuccessful) {
			logger.info("Successfully placed order with ID: {} for user: {}", savedOrder.getId(), userEmail);
			emailService.sendOrderConfirmation(savedOrder);
		} else {
			logger.warn("Order failed due to payment failure simulation. ID: {}", savedOrder.getId());
		}
		
		return savedOrder;
	}

	public List<OrderEntity> getOrders(String userEmail, boolean isAdmin) {
		if (isAdmin) {
			return orderRepository.findAll();
		}
		return orderRepository.findByUserEntity(getUserByEmail(userEmail));
	}

	public OrderEntity getOrderById(Long id, String userEmail, boolean isAdmin) {
		OrderEntity orderEntity = orderRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Order not found"));
		if (!isAdmin && !orderEntity.getUserEntity().getEmail().equalsIgnoreCase(userEmail)) {
			throw new AccessDeniedException("You can only access your own orders");
		}
		return orderEntity;
	}

	public OrderEntity updateOrderStatus(Long id, String orderStatus) {
		if (orderStatus == null || orderStatus.isBlank()) {
			throw new IllegalArgumentException("Order status is required");
		}
		OrderEntity orderEntity = orderRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Order not found"));
		orderEntity.setOrderStatus(orderStatus.trim().toUpperCase());
		return orderRepository.save(orderEntity);
	}

	private UserEntity getUserByEmail(String email) {
		UserEntity userEntity = userRepository.findByEmail(email);
		if (userEntity == null) {
			throw new ResourceNotFoundException("User not found");
		}
		return userEntity;
	}
}
