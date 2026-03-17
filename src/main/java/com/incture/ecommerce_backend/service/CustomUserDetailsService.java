package com.incture.ecommerce_backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import com.incture.ecommerce_backend.entity.UserEntity;
import com.incture.ecommerce_backend.repository.UserRepository;
import com.incture.ecommerce_backend.security.CustomUserDetails;

/*
 * CustomUserDetailsService loads user details from database
 * for Spring Security authentication.
 */

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        UserEntity userEntity = userRepository.findByEmail(email);

        if (userEntity == null) {
            throw new UsernameNotFoundException("User not found");
        }

        return new CustomUserDetails(userEntity);
    }
}
