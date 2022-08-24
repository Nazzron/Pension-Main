package com.cts.pensionerDetails.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.cts.pensionerDetails.exception.InvalidTokenException;
import com.cts.pensionerDetails.feign.AuthorisationClient;
import com.cts.pensionerDetails.model.PensionerDetails;
import com.cts.pensionerDetails.service.PensionerDetailServiceImpl;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@CrossOrigin
public class PensionerDetailsController {

	@Autowired
	private AuthorisationClient authorisationClient;

	@Autowired
	private PensionerDetailServiceImpl pensionerDetailService;


	// Handler Method to fetch the pensioner detail by the Aadhaar number
	@GetMapping("/PensionerDetailByAadhaar/{aadhaarNumber}")
	public PensionerDetails getPensionerDetailByAadhaar(@RequestHeader(name = "Authorization") String token,
			@PathVariable String aadhaarNumber) {
		log.info("START - getPensionerDetailByAadhaar()");
		
		if (!authorisationClient.validate(token)) {
			throw new InvalidTokenException("You are not allowed to access this resource");
		}
		
		log.info("END - getPensionerDetailByAadhaar()");
		return pensionerDetailService.getPensionerDetailByAadhaarNumber(token, aadhaarNumber);

	}

}
