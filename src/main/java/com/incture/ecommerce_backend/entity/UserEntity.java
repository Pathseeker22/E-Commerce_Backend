package com.incture.ecommerce_backend.entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

/*
 * UserEntity represents the users table in the database
 * It stores information about system users
 */

@Entity
@Table(name = "users")
public class UserEntity {

//	Primary key for the user table
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String name;

	@Column(unique = true)
	private String email;
	
	@JsonIgnore
	private String password;
	private String role;

	//One user has one cart
	@JsonIgnore
	@OneToOne(mappedBy = "userEntity")
	private CartEntity cartEntity;

	//One user can place multiple orders
	@JsonIgnore
	@OneToMany(mappedBy = "userEntity")
	private List<OrderEntity> ordersList;

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

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public CartEntity getCartEntity() {
		return cartEntity;
	}

	public void setCartEntity(CartEntity cartEntity) {
		this.cartEntity = cartEntity;
	}

	public List<OrderEntity> getOrdersList() {
		return ordersList;
	}

	public void setOrdersList(List<OrderEntity> ordersList) {
		this.ordersList = ordersList;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public UserEntity(Long id, String name, String password, String role, CartEntity cartEntity,
			List<OrderEntity> ordersList) {
		this.id = id;
		this.name = name;
		this.password = password;
		this.role = role;
		this.cartEntity = cartEntity;
		this.ordersList = ordersList;
	}

	public UserEntity() {
		super();
	}

}
