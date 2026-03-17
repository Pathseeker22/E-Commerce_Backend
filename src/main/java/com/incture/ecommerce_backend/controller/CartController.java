package com.incture.ecommerce_backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.incture.ecommerce_backend.dto.CartItemDTO;
import com.incture.ecommerce_backend.entity.CartEntity;
import com.incture.ecommerce_backend.entity.CartItemEntity;
import com.incture.ecommerce_backend.service.CartService;

/**
 * Controller for managing shopping cart operations.
 */
@RestController
@RequestMapping(path = "/api/cart")
public class CartController {
	
	@Autowired
	private CartService cartService;
	
	@PostMapping(value = "/add/{productId}")
	@PreAuthorize("hasRole('CUSTOMER')")
	public CartItemEntity addProductToCart(@PathVariable(name = "productId") Long productId,
										   @RequestBody CartItemDTO cartItemDTO,
										   Authentication authentication) {
		return cartService.addProductToCart(authentication.getName(), productId, cartItemDTO.getQuantity());
	}
	
	@PutMapping("/update/{productId}")
	@PreAuthorize("hasRole('CUSTOMER')")
	public CartItemEntity updateCartItem(@PathVariable(name = "productId") Long productId,
										 @RequestBody CartItemDTO cartItemDTO,
										 Authentication authentication) {
		return cartService.updateCartItem(authentication.getName(), productId, cartItemDTO.getQuantity());
	}
	
	@DeleteMapping("/remove/{productId}")
	@PreAuthorize("hasRole('CUSTOMER')")
	public String removeProductFromCart(@PathVariable(name = "productId") Long productId, Authentication authentication) {
		cartService.removeProductFromCart(authentication.getName(), productId);
		return "Product removed from cart successfully";
	}
	
	@GetMapping
	@PreAuthorize("hasRole('CUSTOMER')")
	public CartEntity viewCart(Authentication authentication){
		return cartService.getCartByUser(authentication.getName());
	}
}
