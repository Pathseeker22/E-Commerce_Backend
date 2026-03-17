package com.incture.ecommerce_backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.incture.ecommerce_backend.dto.CheckoutRequestDTO;
import com.incture.ecommerce_backend.dto.OrderDTO;
import com.incture.ecommerce_backend.entity.OrderEntity;
import com.incture.ecommerce_backend.service.OrderService;

/**
 * Controller for managing customer orders and checkout processes.
 */

@RestController
@RequestMapping(path = "/api/orders")
public class OrderController {
	
	@Autowired
	private OrderService orderService;
	
	// Checkout API
	@PostMapping(value = "/checkout")
	@PreAuthorize("hasRole('CUSTOMER')")
	public OrderEntity checkout(@RequestBody(required = false) CheckoutRequestDTO checkoutRequest, Authentication authentication){
		String paymentMode = (checkoutRequest != null) ? checkoutRequest.getPaymentMode() : "COD";
		return orderService.checkout(authentication.getName(), paymentMode);
	}

	@GetMapping
	@PreAuthorize("hasAnyRole('ADMIN', 'CUSTOMER')")
	public List<OrderEntity> getOrders(Authentication authentication) {
		return orderService.getOrders(authentication.getName(), isAdmin(authentication));
	}

	@GetMapping("/{id}")
	@PreAuthorize("hasAnyRole('ADMIN', 'CUSTOMER')")
	public OrderEntity getOrderById(@PathVariable(name = "id") Long id, Authentication authentication) {
		return orderService.getOrderById(id, authentication.getName(), isAdmin(authentication));
	}

	@PutMapping("/{id}/status")
	@PreAuthorize("hasRole('ADMIN')")
	public OrderEntity updateOrderStatus(@PathVariable(name = "id") Long id, @RequestBody OrderDTO orderDTO) {
		return orderService.updateOrderStatus(id, orderDTO.getOrderStatus());
	}

	private boolean isAdmin(Authentication authentication) {
		return authentication.getAuthorities().stream()
				.anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));
	}
}
