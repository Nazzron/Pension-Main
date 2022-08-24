package com.cts.authorization.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.cts.authorization.entity.User;


@SpringBootTest
class UserRepositoryTests {

	@Autowired
	private UserRepository userRepository;

	@Test
	void testFindUserById_userExists() {

		final String username = "jugraj";
		Optional<User> userOptional = userRepository.findById(username);
		assertTrue(userOptional.isPresent());
		assertEquals(username, userOptional.get().getUsername());

	}

	@Test
	void testFindUserById_userDoesNotExists() {
		
		final String id = "adminDoesNotExist";
		Optional<User> userOptional = userRepository.findById(id);
		assertTrue(userOptional.isEmpty());
	}
}
