package com.incture.ecommerce_backend.entity;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

/*
 * OrderEntity represents an order placed by a user
 */

@Entity
@Table(name = "orders")
public class OrderEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private Double totalAmount;
	private LocalDateTime orderDate;
	private String paymentStatus;
	private String orderStatus;
	private String paymentMode;
	
	// Order belongs to a user
	@ManyToOne
	@JoinColumn(name = "user_id")
	@JsonIgnoreProperties({"cartEntity", "ordersList", "password"})
	private UserEntity userEntity;
	
	// Order contains multiple order items
	@OneToMany(mappedBy = "orderEntity")
	private List<OrderItemEntity> orderItemEntities;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(Double totalAmount) {
		this.totalAmount = totalAmount;
	}

	public LocalDateTime getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(LocalDateTime orderDate) {
		this.orderDate = orderDate;
	}

	public String getPaymentStatus() {
		return paymentStatus;
	}

	public void setPaymentStatus(String paymentStatus) {
		this.paymentStatus = paymentStatus;
	}

	public String getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}

	public String getPaymentMode() {
		return paymentMode;
	}

	public void setPaymentMode(String paymentMode) {
		this.paymentMode = paymentMode;
	}

	public UserEntity getUserEntity() {
		return userEntity;
	}

	public void setUserEntity(UserEntity userEntity) {
		this.userEntity = userEntity;
	}

	public List<OrderItemEntity> getOrderItemEntities() {
		return orderItemEntities;
	}

	public void setOrderItemEntities(List<OrderItemEntity> orderItemEntities) {
		this.orderItemEntities = orderItemEntities;
	}

	public OrderEntity(Long id, Double totalAmount, LocalDateTime orderDate, String paymentStatus, String orderStatus,
			UserEntity userEntity, List<OrderItemEntity> orderItemEntities) {
		this.id = id;
		this.totalAmount = totalAmount;
		this.orderDate = orderDate;
		this.paymentStatus = paymentStatus;
		this.orderStatus = orderStatus;
		this.userEntity = userEntity;
		this.orderItemEntities = orderItemEntities;
	}

	public OrderEntity() {
		super();
	}
}
