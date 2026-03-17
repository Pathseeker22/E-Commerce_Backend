package com.incture.ecommerce_backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.incture.ecommerce_backend.dto.UserRequestDTO;
import com.incture.ecommerce_backend.dto.UserResponseDTO;
import com.incture.ecommerce_backend.entity.UserEntity;
import com.incture.ecommerce_backend.service.UserService;

/**
 * Controller for handling user registration and profile management.
 */
@RestController
@RequestMapping(path = "/api/users")
public class UserController {

	@Autowired
	private UserService userService;

	@PostMapping("/register")
	public UserResponseDTO registerUser(@RequestBody UserRequestDTO userRequestDTO) { 
	    return userService.registerUser(userRequestDTO);
	}

	@GetMapping
	@PreAuthorize("hasRole('ADMIN')")
	public java.util.List<UserResponseDTO> getAllUsers() {
		return userService.getAllUsers();
	}

	@GetMapping(value = "/{id}")
	@PreAuthorize("isAuthenticated()")
	public UserResponseDTO getUserById(@PathVariable(name = "id") Long id, Authentication authentication) {
		return userService.getUserById(id, authentication.getName(), isAdmin(authentication));
	}

	@PutMapping(value = "/{id}")
	@PreAuthorize("isAuthenticated()")
	public UserResponseDTO updateUser(@PathVariable(name = "id") Long id,
									  @RequestBody UserEntity userEntity,
									  Authentication authentication) {
		return userService.updateUser(id, userEntity, authentication.getName(), isAdmin(authentication));
	}

	@DeleteMapping(value = "/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public String deleteUser(@PathVariable(name = "id") Long id) {
		userService.deleteUser(id);
		return "User deleted successfully";
	}

	private boolean isAdmin(Authentication authentication) {
		return authentication.getAuthorities().stream()
				.anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));
	}
}
