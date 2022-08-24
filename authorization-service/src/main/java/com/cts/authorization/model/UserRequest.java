package com.cts.authorization.model;

import javax.validation.constraints.NotBlank;

import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Component
public class UserRequest {

	@NotBlank(message = "Please enter a valid Username")
	private String username;

	@NotBlank(message = "Please enter a valid Password")
	private String password;
}
