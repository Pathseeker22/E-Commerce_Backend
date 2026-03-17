package com.incture.ecommerce_backend.entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

/*
 * ProductEntity represents products availble in the e-commerce store
 * It stores product details such as name, price, stock, category, and rating
 */

@Entity
@Table(name = "products")
public class ProductEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String name;
	private String description;
	private Double price;
	private Integer stock;
	private String category;
	private String imageUrl;
	private Double rating;

	/// A product can appear in multiple cart items
	@JsonIgnore
	@OneToMany(mappedBy = "productEntity", cascade = CascadeType.ALL)
	private List<CartItemEntity> cartItemEntities;

	// A product can appear in multiple order items
	@JsonIgnore
	@OneToMany(mappedBy = "productEntity", cascade = CascadeType.ALL)
	private List<OrderItemEntity> orderItemEntities;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public Integer getStock() {
		return stock;
	}

	public void setStock(Integer stock) {
		this.stock = stock;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public Double getRating() {
		return rating;
	}

	public void setRating(Double rating) {
		this.rating = rating;
	}

	public List<CartItemEntity> getCartItemEntities() {
		return cartItemEntities;
	}

	public void setCartItemEntities(List<CartItemEntity> cartItemEntities) {
		this.cartItemEntities = cartItemEntities;
	}

	public List<OrderItemEntity> getOrderItemEntities() {
		return orderItemEntities;
	}

	public void setOrderItemEntities(List<OrderItemEntity> orderItemEntities) {
		this.orderItemEntities = orderItemEntities;
	}

	public ProductEntity() {
		super();
	}

	public ProductEntity(Long id, String name, String description, Double price, Integer stock, String category,
			String imageUrl, Double rating, List<CartItemEntity> cartItemEntities,
			List<OrderItemEntity> orderItemEntities) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.price = price;
		this.stock = stock;
		this.category = category;
		this.imageUrl = imageUrl;
		this.rating = rating;
		this.cartItemEntities = cartItemEntities;
		this.orderItemEntities = orderItemEntities;
	}
}
