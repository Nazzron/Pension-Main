package com.cts.pensionerDetails.Controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.text.ParseException;
import java.util.Collections;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.cts.pensionerDetails.exception.ErrorResponse;
import com.cts.pensionerDetails.exception.NotFoundException;
import com.cts.pensionerDetails.feign.AuthorisationClient;
import com.cts.pensionerDetails.model.BankDetails;
import com.cts.pensionerDetails.model.PensionerDetails;
import com.cts.pensionerDetails.service.PensionerDetailServiceImpl;
import com.cts.pensionerDetails.util.DateUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import feign.FeignException;
import feign.Request;
import feign.Request.HttpMethod;

@WebMvcTest
class PensionDetailsControllerTest {

	@Value("${errorMessage}")
	private String AADHAAR_NUMBER_NOT_FOUND;

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	private PensionerDetailServiceImpl service;

	@MockBean
	private AuthorisationClient authorisationClient;

	// setup for Pensioner Details input
	@BeforeEach
	void setup() throws ParseException {

		when(authorisationClient.validate(ArgumentMatchers.anyString())).thenReturn(true);

	}

	@Test
	void testToGetCorrectPensionerDetailsFromController() throws Exception {

		final String aadhaarNumber = "111112222213";

		PensionerDetails pensionerDetail = new PensionerDetails("Jugraj", DateUtil.parseDate("17-01-2000"),
				"GOKU2572Q", 50000, 10000, "self", new BankDetails("SBI", 12345678, "public"));

		when(service.getPensionerDetailByAadhaarNumber("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9", aadhaarNumber))
				.thenReturn(pensionerDetail);

		mockMvc.perform(get("/PensionerDetailByAadhaar/{aadhaarNumber}", aadhaarNumber)
				.header("Authorization", "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9").accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$.name", Matchers.equalTo("Jugraj")))
				.andExpect(jsonPath("$.pan", Matchers.equalTo("GOKU2572Q")))
				.andExpect(jsonPath("$.dateOfBirth", Matchers.equalTo("2000-01-17")))
				.andExpect(jsonPath("$.bank.accountNumber", Matchers.equalTo(12345678)));


	}

	@Test
	void testForAadharNumberNotInCsvFile() throws Exception {


		final String aadhaarNumber = "12345678888";

		when(service.getPensionerDetailByAadhaarNumber(ArgumentMatchers.any(), ArgumentMatchers.any()))
				.thenThrow(new NotFoundException(AADHAAR_NUMBER_NOT_FOUND));

		mockMvc.perform(get("/PensionerDetailByAadhaar/{aadhaarNumber}", aadhaarNumber)
				.header("Authorization", "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9").accept(MediaType.APPLICATION_JSON))
				.andExpect(status().is4xxClientError())
				.andExpect(jsonPath("$.message", Matchers.equalTo(AADHAAR_NUMBER_NOT_FOUND)));

	}

	


	@Test
	void testPensionerDetails_withValidFeignResponse() throws JsonProcessingException, Exception {

		final String aadhaarNumber = "123456789098";


		when(service.getPensionerDetailByAadhaarNumber(ArgumentMatchers.any(), ArgumentMatchers.any()))
				.thenThrow(new FeignException.BadRequest("Service is offline",
						Request.create(HttpMethod.GET, "", Collections.emptyMap(), null, null, null),
						objectMapper.writeValueAsBytes(new ErrorResponse("Internal Server Error"))));

		// performing test
		mockMvc.perform(get("/PensionerDetailByAadhaar/{aadhaarNumber}", aadhaarNumber)
				.accept(MediaType.APPLICATION_JSON).header("Authorization", "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9"))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.message", Matchers.equalTo("Internal Server Error")));

	}



	
}
