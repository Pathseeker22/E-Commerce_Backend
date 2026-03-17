package com.incture.ecommerce_backend.exception;

/*
 * Custom exception thrown when a resource is not found
 */

public class ResourceNotFoundException extends RuntimeException{
	
	public ResourceNotFoundException(String message) {
		super(message);
	}
}
