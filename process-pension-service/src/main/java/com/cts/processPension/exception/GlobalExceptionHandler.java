package com.cts.processPension.exception;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import feign.FeignException;
import lombok.extern.slf4j.Slf4j;


@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
	@Autowired
	private ObjectMapper objectMapper;

	@PostConstruct
	public void setUp() {
		objectMapper.registerModule(new JavaTimeModule());
	}


	// For input validation errors and Invalid input entered
	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {

		log.error("Handling method argement not valid in Process pension microservice");
		
		ErrorResponse response = new ErrorResponse();
		
		response.setMessage("Invalid Credentials");
		response.setTimestamp(LocalDateTime.now());

		List<String> errors = ex.getBindingResult().getFieldErrors().stream().map(x -> x.getDefaultMessage())
				.collect(Collectors.toList());

		response.setFieldErrors(errors);
	
		log.error(errors.toString());
		
		return new ResponseEntity<>(response, headers, status);
	}


	//Feign Exception
	@ExceptionHandler(FeignException.class)
	public ResponseEntity<ErrorResponse> handleFeignStatusException(FeignException exception,
			HttpServletResponse response) {
	
		log.error("Handling Feign Clien");
		
		log.debug("Message: {}", exception.getMessage());
		
		ErrorResponse errorResponse;
		
		log.debug("UTF-8 Message: {}", exception.contentUTF8());
		
		if (exception.contentUTF8().isBlank()) {
			errorResponse = new ErrorResponse("Invalid Request");
		} 
		else {
			try {
				
				errorResponse = objectMapper.readValue(exception.contentUTF8(), ErrorResponse.class);
				log.debug("Success in parsing the error...");
		
			} catch (JsonProcessingException e) {
			
				errorResponse = new ErrorResponse(exception.contentUTF8());
				log.error("Processing Error {}", e.toString());
			
			}
		}
		return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
	}


	// NotFoundException
	@ExceptionHandler(NotFoundException.class)
	public ResponseEntity<ErrorResponse> handleNotFoundException(NotFoundException exception,
			HttpServletResponse response) {
	
		log.error("Handling Details mismatch exception");
	
		ErrorResponse errorResponse = new ErrorResponse();
		
		errorResponse.setMessage(exception.getMessage());
		errorResponse.setTimestamp(LocalDateTime.now());
		errorResponse.setFieldErrors(Collections.singletonList(exception.getMessage()));
		
		return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
	}


	// InvalidTokenException
	@ExceptionHandler(InvalidTokenException.class)
	public ResponseEntity<ErrorResponse> handleInvalidTokenException(InvalidTokenException exception,
			HttpServletResponse response) {
		
		log.error("Handling Invalid Token exception");
		
		ErrorResponse errorResponse = new ErrorResponse();
		
		errorResponse.setMessage(exception.getMessage());
		errorResponse.setTimestamp(LocalDateTime.now());
		errorResponse.setFieldErrors(Collections.singletonList(exception.getMessage()));
		
		return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
	}

}
