package com.cts.authorization.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Collection;
import java.util.Collections;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.cts.authorization.exception.InvalidTokenException;
import com.cts.authorization.model.UserRequest;
import com.cts.authorization.service.UserServiceImpl;
import com.cts.authorization.util.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;


@WebMvcTest
class AuthorizationControllerTests {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private UserServiceImpl userServiceImpl;

	@MockBean
	private JwtUtil jwtUtil;

	@MockBean
	private AuthenticationManager authenticationManager;

	@Value("${userDetails.errorMessage}")
	private String ERROR_MESSAGE;

	@Value("${userDetails.badCredentialsMessage}")
	private String BAD_CREDENTIALS_MESSAGE;

	@Value("${userDetails.disabledAccountMessage}")
	private String DISABLED_ACCOUNT_MESSAGE;

	@Value("${userDetails.lockedAccountMessage}")
	private String LOCKED_ACCOUNT_MESSAGE;

	private static ObjectMapper mapper = new ObjectMapper();
	private static SecurityUser validUser;

	@BeforeEach
	void generateUserCredentials() {
		
		// User object
		validUser = new SecurityUser("jugraj", "$2a$04$H2JjDTxlvGrKvaMURhHhoeacX0tSB0O03Y4rYHwTifD0RtAzeBoTa",
				Collections.singletonList(new SimpleGrantedAuthority("ADMIN")));
		
		}

	
	@Test
	void testLogin_withValidCredentials() throws Exception {

		// Set the user request
		UserRequest user = new UserRequest("jugraj", "jugraj");

		String token = "eyJhbGciOiJIUzI1NiJ9.eyJpYXQiOjE2MjcwMzk2NzcsInN1YiI6ImFkbWluMSIsImV4cCI6MTY1ODU3NTY3N30.trkCUngtLG8C1W6obvcGvQhCK1J9qg2Hsbcn8GJB95Y";

		when(authenticationManager.authenticate(ArgumentMatchers.any()))
				.thenReturn(new TestingAuthenticationToken("jugraj", "jugraj", "ADMIN"));
		
		when(jwtUtil.generateToken(ArgumentMatchers.any())).thenReturn(token);

		String json = mapper.writeValueAsString(user);

		MvcResult result = mockMvc.perform(post("/login").contentType(MediaType.APPLICATION_JSON)
				.characterEncoding("UTF-8").content(json).accept(MediaType.TEXT_PLAIN)).andExpect(status().isOk())
				.andReturn();

		String contentAsString = result.getResponse().getContentAsString();

		assertNotNull(contentAsString);

		assertEquals(contentAsString, token);

	}
	@Test
	void testLogin_withInvalidCredentials() throws Exception {

		// Set the invalid user request and role
		UserRequest user = new UserRequest("JohnCena", "wwwe");

		// mock certain functionalities to return a valid user and generate the token
		when(authenticationManager.authenticate(ArgumentMatchers.any()))
				.thenThrow(new BadCredentialsException(ERROR_MESSAGE));

		String json = mapper.writeValueAsString(user);

	
		mockMvc.perform(post("/login").contentType(MediaType.APPLICATION_JSON).characterEncoding("UTF-8").content(json)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().is4xxClientError())
				.andExpect(jsonPath("$.message", Matchers.equalTo(ERROR_MESSAGE)));

	}



	//token validation tests
	
	@Test
	void testValidateAdmin_withValidTokenAndRole() throws Exception {

		// mock certain functionalities to load user and have a valid token
		when(userServiceImpl.loadUserByUsername(ArgumentMatchers.any())).thenReturn(validUser);
		when(jwtUtil.isTokenExpiredOrInvalidFormat(ArgumentMatchers.any())).thenReturn(false);

		// set the token
		String token = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJpYXQiOjE2MjcwMzk2NzcsInN1YiI6ImFkbWluMSIsImV4cCI6MTY1ODU3NTY3N30.trkCUngtLG8C1W6obvcGvQhCK1J9qg2Hsbcn8GJB95Y";

		// perform the mock
		mockMvc.perform(get("/validate").header(HttpHeaders.AUTHORIZATION, token)).andExpect(status().isOk());

	}

	@Test
	void testValidate_withInvalidToken() throws Exception {
		final String errorMessage = "Token has been expired";

		// mock certain functionalities to load user and have a invalid token
		when(userServiceImpl.loadUserByUsername(ArgumentMatchers.any())).thenReturn(validUser);
		when(jwtUtil.isTokenExpiredOrInvalidFormat(ArgumentMatchers.any()))
				.thenThrow(new InvalidTokenException(errorMessage));

		// set the invalid token
		String token = "Bearer ghjKJHGFDFYUjcvjkOIfg.gHJGugvbn789Vikj7KJhvb0.HJNu6GVCk87TGBnm";

		// perform the mock
		mockMvc.perform(get("/validate").header(HttpHeaders.AUTHORIZATION, token)).andExpect(status().isBadRequest());

	}

	public class SecurityUser extends org.springframework.security.core.userdetails.User {

		private static final long serialVersionUID = -4209816021578748288L;

		public SecurityUser(String username, String password, Collection<? extends GrantedAuthority> authorities) {
			super(username, password, authorities);
		}

	}

}
