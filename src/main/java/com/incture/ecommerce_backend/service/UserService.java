package com.incture.ecommerce_backend.service;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.incture.ecommerce_backend.dto.UserRequestDTO;
import com.incture.ecommerce_backend.dto.UserResponseDTO;
import com.incture.ecommerce_backend.entity.CartEntity;
import com.incture.ecommerce_backend.entity.UserEntity;
import com.incture.ecommerce_backend.exception.ResourceNotFoundException;
import com.incture.ecommerce_backend.repository.CartRepository;
import com.incture.ecommerce_backend.repository.UserRepository;

/**
 * Service class for handling user-related business operations.
 */

@Service
public class UserService {

	private static final Logger logger = LoggerFactory.getLogger(UserService.class);

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private CartRepository cartRepository;
	
	// Save new user to database
	public UserResponseDTO registerUser(UserRequestDTO userRequestDTO) {
		if (userRepository.findByEmail(userRequestDTO.getEmail()) != null) {
			logger.warn("Registration attempt failed: Email {} already exists", userRequestDTO.getEmail());
			throw new IllegalArgumentException("Email is already registered");
		}
		
		logger.info("Registering new user with email: {}", userRequestDTO.getEmail());
		UserEntity userEntity = modelMapper.map(userRequestDTO, UserEntity.class);
		userEntity.setPassword(passwordEncoder.encode(userRequestDTO.getPassword()));
		userEntity.setRole(resolveRequestedRole(userRequestDTO.getRole()));
		
		userEntity = userRepository.save(userEntity);
		createCartForUser(userEntity);
		
		// Entity -> DTO
		return toUserResponseDTO(userEntity);
	}

	// Get user by Id
	public UserResponseDTO getUserById(Long id, String requesterEmail, boolean isAdmin) {
		UserEntity userEntity = getUserEntityById(id);
		validateUserAccess(userEntity, requesterEmail, isAdmin);
		return toUserResponseDTO(userEntity);
	}

	// List all users (Admin only)
	public java.util.List<UserResponseDTO> getAllUsers() {
		return userRepository.findAll().stream()
				.map(this::toUserResponseDTO)
				.collect(java.util.stream.Collectors.toList());
	}

	public UserEntity getUserEntityById(Long id) {
		return userRepository.findById(id).orElseThrow(() ->
				new ResourceNotFoundException("User not found")
				);
	}

	public UserEntity getUserEntityByEmail(String email) {
		UserEntity userEntity = userRepository.findByEmail(email);
		if (userEntity == null) {
			throw new ResourceNotFoundException("User not found");
		}
		return userEntity;
	}

	// Updating user through Id and UserEntity
	public UserResponseDTO updateUser(Long id, UserEntity userEntity, String requesterEmail, boolean isAdmin) {
		UserEntity existingUser = getUserEntityById(id);
		validateUserAccess(existingUser, requesterEmail, isAdmin);
		
		if(userEntity.getName() != null) {
			existingUser.setName(userEntity.getName());
		}
		if(userEntity.getEmail() != null) {
			UserEntity userWithEmail = userRepository.findByEmail(userEntity.getEmail());
			if (userWithEmail != null && !userWithEmail.getId().equals(existingUser.getId())) {
				throw new IllegalArgumentException("Email is already registered");
			}
			existingUser.setEmail(userEntity.getEmail());
		}
		if(userEntity.getPassword() != null && !userEntity.getPassword().isBlank()) {
			existingUser.setPassword(passwordEncoder.encode(userEntity.getPassword()));
		}
		if(userEntity.getRole() != null && !userEntity.getRole().isBlank()) {
			if (!isAdmin) {
				throw new AccessDeniedException("Only admin can change roles");
			}
			existingUser.setRole(resolveRequestedRole(userEntity.getRole()));
		}
		
		return toUserResponseDTO(userRepository.save(existingUser));
	}

	// Delete user through Id
	public void deleteUser(Long id) {
		userRepository.deleteById(id);
	}

	private UserResponseDTO toUserResponseDTO(UserEntity userEntity) {
		return modelMapper.map(userEntity, UserResponseDTO.class);
	}

	private String normalizeRole(String role) {
		return role.trim().toUpperCase().replace("ROLE_", "");
	}

	private String resolveRequestedRole(String role) {
		if (role == null || role.isBlank()) {
			return "CUSTOMER";
		}

		String normalizedRole = normalizeRole(role);
		if (!normalizedRole.equals("ADMIN") && !normalizedRole.equals("CUSTOMER")) {
			throw new IllegalArgumentException("Role must be ADMIN or CUSTOMER");
		}
		return normalizedRole;
	}

	private void validateUserAccess(UserEntity targetUser, String requesterEmail, boolean isAdmin) {
		if (!isAdmin && !targetUser.getEmail().equalsIgnoreCase(requesterEmail)) {
			throw new AccessDeniedException("You can only access your own profile");
		}
	}

	private void createCartForUser(UserEntity userEntity) {
		CartEntity cartEntity = new CartEntity();
		cartEntity.setUserEntity(userEntity);
		cartEntity.setTotalPrice(0.0);
		cartRepository.save(cartEntity);
	}

}
