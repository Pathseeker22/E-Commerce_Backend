package com.incture.ecommerce_backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

/*
 * CartItemEntity represents an item inside the shopping cart
 */

@Entity
@Table(name = "cart_items")
public class CartItemEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private Integer quantity;

	// Cart item belongs to a cart
	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "cart_id")
	private CartEntity cartEntity;

	// Cart item references a product
	@ManyToOne
	@JoinColumn(name = "product_id")
	private ProductEntity productEntity;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public CartEntity getCartEntity() {
		return cartEntity;
	}

	public void setCartEntity(CartEntity cartEntity) {
		this.cartEntity = cartEntity;
	}

	public ProductEntity getProductEntity() {
		return productEntity;
	}

	public void setProductEntity(ProductEntity productEntity) {
		this.productEntity = productEntity;
	}

	public CartItemEntity(Long id, Integer quantity, CartEntity cartEntity, ProductEntity productEntity) {
		this.id = id;
		this.quantity = quantity;
		this.cartEntity = cartEntity;
		this.productEntity = productEntity;
	}

	public CartItemEntity() {
		super();
	}

}
