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
 * OrderItemEntity represents an product item inside a placed order
 */

@Entity
@Table(name = "order_items")
public class OrderItemEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private Integer quantity;
	private Double price;

	// Order item belongs to an order
	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "order_id")
	private OrderEntity orderEntity;

	// Order item references a product
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

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public OrderEntity getOrderEntity() {
		return orderEntity;
	}

	public void setOrderEntity(OrderEntity orderEntity) {
		this.orderEntity = orderEntity;
	}

	public ProductEntity getProductEntity() {
		return productEntity;
	}

	public void setProductEntity(ProductEntity productEntity) {
		this.productEntity = productEntity;
	}

	public OrderItemEntity(Long id, Integer quantity, Double price, OrderEntity orderEntity,
			ProductEntity productEntity) {
		this.id = id;
		this.quantity = quantity;
		this.price = price;
		this.orderEntity = orderEntity;
		this.productEntity = productEntity;
	}

	public OrderItemEntity() {
		super();
	}

}
