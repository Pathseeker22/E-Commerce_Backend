package com.incture.ecommerce_backend.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import com.incture.ecommerce_backend.entity.OrderEntity;
import com.incture.ecommerce_backend.entity.UserEntity;

@ExtendWith(MockitoExtension.class)
public class EmailServiceTest {

    @Mock
    private JavaMailSender mailSender;

    @InjectMocks
    private EmailService emailService;

    @Test
    void whenSendEmail_thenSuccess() {
        emailService.sendEmail("test@example.com", "Subject", "Body");
        verify(mailSender, times(1)).send(any(SimpleMailMessage.class));
    }

    @Test
    void whenSendOrderConfirmation_thenSuccess() {
        UserEntity user = new UserEntity();
        user.setEmail("user@example.com");
        user.setName("John Doe");

        OrderEntity order = new OrderEntity();
        order.setId(101L);
        order.setUserEntity(user);
        order.setTotalAmount(250.0);
        order.setOrderStatus("PENDING");

        emailService.sendOrderConfirmation(order);
        verify(mailSender, times(1)).send(any(SimpleMailMessage.class));
    }
}
