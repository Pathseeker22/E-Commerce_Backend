package com.incture.ecommerce_backend.dto;

public class CheckoutRequestDTO {
    private String paymentMode;

    public String getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(String paymentMode) {
        this.paymentMode = paymentMode;
    }
}
