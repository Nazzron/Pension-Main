package com.cts.authorization.service;

import java.util.Collections;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import com.cts.authorization.entity.User;
import com.cts.authorization.exception.InvalidCredentialsException;
import com.cts.authorization.repository.UserRepository;

@Service
public class UserServiceImpl implements UserDetailsService {

	@Value("${userDetails.errorMessage}")
	private String USER_DOES_NOT_EXIST_MESSAGE;

	@Autowired
	private UserRepository userRepository;

	// gets the user based on the username if it exists in the database
	public Optional<User> findByUsername(String username) {
		return userRepository.findById(username);
	}

	// load the details & compares username in DB.
	@Override
	public UserDetails loadUserByUsername(String username) {

		Optional<User> userOptional = findByUsername(username);
		if (!userOptional.isPresent()) {
	
			throw new InvalidCredentialsException(USER_DOES_NOT_EXIST_MESSAGE);
		} else {

			User user = userOptional.get();
			
			return new org.springframework.security.core.userdetails.User(username, user.getPassword(),
					Collections.singletonList(new SimpleGrantedAuthority(user.getRole())));
		}
	}

}
