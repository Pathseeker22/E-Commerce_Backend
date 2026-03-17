package com.incture.ecommerce_backend.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.incture.ecommerce_backend.dto.UserRequestDTO;
import com.incture.ecommerce_backend.dto.UserResponseDTO;
import com.incture.ecommerce_backend.entity.UserEntity;
import com.incture.ecommerce_backend.repository.CartRepository;
import com.incture.ecommerce_backend.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private CartRepository cartRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private UserService userService;

    @Test
    void whenRegisterUser_thenSucceed() {
        UserRequestDTO request = new UserRequestDTO();
        request.setEmail("test@example.com");
        request.setPassword("password123");
        request.setName("Test User");

        UserEntity entity = new UserEntity();
        entity.setEmail("test@example.com");

        UserResponseDTO responseDTO = new UserResponseDTO();
        responseDTO.setEmail("test@example.com");

        when(userRepository.findByEmail("test@example.com")).thenReturn(null);
        when(passwordEncoder.encode(anyString())).thenReturn("hashed_password");
        when(modelMapper.map(any(UserRequestDTO.class), eq(UserEntity.class))).thenReturn(entity);
        when(userRepository.save(any(UserEntity.class))).thenReturn(entity);
        when(modelMapper.map(any(UserEntity.class), eq(UserResponseDTO.class))).thenReturn(responseDTO);

        UserResponseDTO result = userService.registerUser(request);

        assertNotNull(result);
        assertEquals("test@example.com", result.getEmail());
        verify(userRepository, times(1)).save(any());
    }

    @Test
    void whenGetUserByEmail_thenReturnsUser() {
        UserEntity entity = new UserEntity();
        entity.setEmail("test@example.com");

        when(userRepository.findByEmail("test@example.com")).thenReturn(entity);

        UserEntity result = userRepository.findByEmail("test@example.com");

        assertNotNull(result);
        assertEquals("test@example.com", result.getEmail());
    }
}
