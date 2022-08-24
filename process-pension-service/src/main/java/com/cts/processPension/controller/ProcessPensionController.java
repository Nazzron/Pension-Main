package com.cts.processPension.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.cts.processPension.entity.PensionDetail;
import com.cts.processPension.entity.PensionerInput;
import com.cts.processPension.exception.InvalidTokenException;
import com.cts.processPension.feign.AuthorisationClient;
import com.cts.processPension.service.ProcessPensionServiceImpl;

import lombok.extern.slf4j.Slf4j;


@RestController
@Slf4j
@CrossOrigin
public class ProcessPensionController {

	@Autowired
	ProcessPensionServiceImpl processPensionService;

	@Autowired
	AuthorisationClient authorisationClient;


	// Processing pension after getting details of user
	@PostMapping("/ProcessPension")
	public ResponseEntity<PensionDetail> getPensionDetails(@RequestHeader(name = "Authorization") String token,
			@RequestBody @Valid PensionerInput pensionerInput) {
	
		log.info("START - getPensionDetails()");
		
		if (!authorisationClient.validate(token)) {
		
			throw new InvalidTokenException("You are not allowed to access this resource");
		
		}
		
		log.info("END - getPensionDetails()");
		
		return new ResponseEntity<>(processPensionService.getPensionDetails(token, pensionerInput), HttpStatus.OK);
	}

}