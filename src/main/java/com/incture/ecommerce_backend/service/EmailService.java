package com.incture.ecommerce_backend.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.incture.ecommerce_backend.entity.OrderEntity;

/**
 * Service for handling email notifications.
 */
@Service
public class EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    @Autowired
    private JavaMailSender mailSender;

    public void sendEmail(String to, String subject, String body) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject(subject);
            message.setText(body);
            mailSender.send(message);
            logger.info("Email sent successfully to: {}", to);
        } catch (Exception e) {
            logger.error("Failed to send email to {}: {}", to, e.getMessage());
        }
    }

    public void sendOrderConfirmation(OrderEntity order) {
        if (order.getUserEntity() == null || order.getUserEntity().getEmail() == null) {
            logger.warn("Order #{} has no user email, skipping notification.", order.getId());
            return;
        }

        String to = order.getUserEntity().getEmail();
        String subject = "Order Confirmation - Order #" + order.getId();
        String body = String.format(
            "Dear %s,\n\n" +
            "Thank you for your order! Your order #%d has been placed successfully.\n" +
            "Total Amount: $%.2f\n" +
            "Order Status: %s\n\n" +
            "We will notify you once your items are shipped.\n\n" +
            "Best regards,\nE-Commerce Team",
            order.getUserEntity().getName(),
            order.getId(),
            order.getTotalAmount(),
            order.getOrderStatus()
        );
        
        sendEmail(to, subject, body);
    }
}
