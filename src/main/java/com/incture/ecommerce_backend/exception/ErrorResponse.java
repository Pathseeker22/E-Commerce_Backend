package com.incture.ecommerce_backend.exception;

/*
 * ErrorResponse is returned when an exception occurs
 */

public class ErrorResponse {
	private String message;
	private int status;
	
	public ErrorResponse(String message, int status) {
		this.message = message;
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public int getStatus() {
		return status;
	}
	
	
}
