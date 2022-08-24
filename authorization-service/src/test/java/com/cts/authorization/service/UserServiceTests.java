package com.cts.authorization.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.cts.authorization.entity.User;
import com.cts.authorization.exception.InvalidCredentialsException;
import com.cts.authorization.repository.UserRepository;


@SpringBootTest
class UserServiceTests {

	@Autowired
	private UserServiceImpl userServiceImpl;

	@MockBean
	private UserRepository userRepository;

	@Value("${userDetails.errorMessage}")
	private String ERROR_MESSAGE;

	@Test
	void testLoadUserByUsername_validUsername() {

		User user = new User("jugraj", "$2a$04$H2JjDTxlvGrKvaMURhHhoeacX0tSB0O03Y4rYHwTifD0RtAzeBoTa", "ADMIN");

		Optional<User> userOptional = Optional.of(user);

		final String id = "jugraj";

		SecurityUser securityUser = new SecurityUser(id, user.getPassword(),
				Collections.singletonList(new SimpleGrantedAuthority(user.getRole())));

		when(userRepository.findById(id)).thenReturn(userOptional);

		// checking condition
		assertEquals(userServiceImpl.loadUserByUsername(id), securityUser);
		assertNotNull(securityUser);

	}

	@Test
	void testLoadUserByUsername_invalidUsername() {

		User user = new User("jugraj", "$2a$04$H2JjDTxlvGrKvaMURhHhoeacX0tSB0O03Y4rYHwTifD0RtAzeBoTa", "ADMIN");

		// Data to mock
		Optional<User> userOptional = Optional.empty();

		final String id = "admin404";

		SecurityUser securityUser = new SecurityUser(id, user.getPassword(),
				Collections.singletonList(new SimpleGrantedAuthority(user.getRole())));

		// Mock the repository
		when(userRepository.findById(id)).thenReturn(userOptional);

		InvalidCredentialsException thrownException = assertThrows(InvalidCredentialsException.class,
				() -> userServiceImpl.loadUserByUsername(id));

		assertTrue(thrownException.getMessage().contains(ERROR_MESSAGE));
		assertNotNull(securityUser);

	}

	@MockBean
	public class SecurityUser extends org.springframework.security.core.userdetails.User {

		private static final long serialVersionUID = -4209816021578748288L;

		public SecurityUser(String username, String password, Collection<? extends GrantedAuthority> authorities) {
			super(username, password, authorities);
		}

	}
}
