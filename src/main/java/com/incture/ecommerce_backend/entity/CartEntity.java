package com.incture.ecommerce_backend.entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

/*
 * CartEntity represents a shopping cart for a user
 * Each user has one cart that contains multiple cart items
 */
@Entity
@Table(name = "carts")
public class CartEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private Double totalPrice;
	
	// Cart belongs to a specific user
	@JsonIgnore
	@OneToOne
	@JoinColumn(name = "user_id")
	private UserEntity userEntity;
	
	// Cart can contain multiple cart items
	@OneToMany(mappedBy = "cartEntity", cascade = CascadeType.ALL)
	private List<CartItemEntity> cartItemEntities;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Double getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(Double totalPrice) {
		this.totalPrice = totalPrice;
	}

	public UserEntity getUserEntity() {
		return userEntity;
	}

	public void setUserEntity(UserEntity userEntity) {
		this.userEntity = userEntity;
	}

	public List<CartItemEntity> getCartItemEntities() {
		return cartItemEntities;
	}

	public void setCartItemEntities(List<CartItemEntity> cartItemEntities) {
		this.cartItemEntities = cartItemEntities;
	}

	public CartEntity(Long id, Double totalPrice, UserEntity userEntity, List<CartItemEntity> cartItemEntities) {
		this.id = id;
		this.totalPrice = totalPrice;
		this.userEntity = userEntity;
		this.cartItemEntities = cartItemEntities;
	}

	public CartEntity() {
		super();
	}
	
	
}
