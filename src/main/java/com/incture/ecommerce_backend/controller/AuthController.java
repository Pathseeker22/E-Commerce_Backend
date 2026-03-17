package com.incture.ecommerce_backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.incture.ecommerce_backend.dto.AuthRequest;
import com.incture.ecommerce_backend.dto.AuthResponse;
import com.incture.ecommerce_backend.utils.JwtUtil;

/**
 * Handles user authentication and session management.
 */

@RestController
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public AuthController(AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping({"/api/auth/login", "/api/users/login"})
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest authRequest) {

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(
                        authRequest.getEmail(),
                        authRequest.getPassword());

        authenticationManager.authenticate(authenticationToken);

        return ResponseEntity.ok(new AuthResponse(jwtUtil.generateToken(authRequest.getEmail())));
    }
}
