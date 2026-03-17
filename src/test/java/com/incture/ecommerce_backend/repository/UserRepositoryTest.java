package com.incture.ecommerce_backend.repository;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.incture.ecommerce_backend.entity.UserEntity;

/**
 * Data JPA tests for User Repository.
 */
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void whenSaveUser_thenCanFindByEmail() {
        UserEntity user = new UserEntity();
        user.setName("John Repository");
        user.setEmail("repo@test.com");
        user.setPassword("hashed_pass");
        user.setRole("CUSTOMER");

        userRepository.save(user);

        UserEntity found = userRepository.findByEmail("repo@test.com");
        assertNotNull(found);
        assertEquals("John Repository", found.getName());
    }
}
