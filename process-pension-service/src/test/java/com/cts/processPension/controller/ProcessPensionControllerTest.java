package com.cts.processPension.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.text.ParseException;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.cts.processPension.entity.PensionDetail;
import com.cts.processPension.entity.PensionerInput;
import com.cts.processPension.exception.NotFoundException;
import com.cts.processPension.feign.AuthorisationClient;
import com.cts.processPension.feign.PensionerDetailsClient;
import com.cts.processPension.service.ProcessPensionServiceImpl;
import com.cts.processPension.util.DateUtil;
import com.fasterxml.jackson.databind.ObjectMapper;


@WebMvcTest(ProcessPensionController.class)
class ProcessPensionControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private AuthorisationClient authorisationClient;

	@MockBean
	private PensionerDetailsClient pensionerDetailsClient;

	@MockBean
	private ProcessPensionServiceImpl processPensionService;

	@Autowired
	private ObjectMapper objectMapper;

	private PensionerInput validPensionerInput;
	private PensionerInput invalidPensionerInput;
	private PensionDetail pensionDetail;

	@BeforeEach
	void setup() throws ParseException {

		validPensionerInput = new PensionerInput("300546467895", "Jugraj", DateUtil.parseDate("17-01-2000"),
				"GOKU2572Q");

		// invalid PensionerInput
		invalidPensionerInput = new PensionerInput("", "Jugraj", DateUtil.parseDate("17-01-2000"), "GOKU2572Q");

		// correct PensionDetail
		pensionDetail = new PensionDetail("GOKU2572Q", 50000, 500, "family");

		// mock authorization microservice response
		when(authorisationClient.validate(ArgumentMatchers.anyString())).thenReturn(true);

	}

	@Test
	void testGetPensionDetails_withValidInput() throws Exception {


		when(processPensionService.getPensionDetails(ArgumentMatchers.any(), ArgumentMatchers.any()))
				.thenReturn(pensionDetail);

		// performing test
		mockMvc.perform(post("/ProcessPension").contentType(MediaType.APPLICATION_JSON).characterEncoding("utf-8")
				.header("Authorization", "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9")
				.content(objectMapper.writeValueAsString(validPensionerInput)).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$.pensionAmount", Matchers.equalTo(50000.0)));

	}

	@Test
	void testGetPensionDetails_withInvalidToken() throws Exception {

		// mock authorization microservice response for invalid token
		when(authorisationClient.validate(ArgumentMatchers.anyString())).thenReturn(false);

		// performing test
		mockMvc.perform(post("/ProcessPension").contentType(MediaType.APPLICATION_JSON).characterEncoding("utf-8")
				.content(objectMapper.writeValueAsString(validPensionerInput)).accept(MediaType.APPLICATION_JSON)
				.header("Authorization", "inavlidtoken1")).andExpect(status().isForbidden());
	}

	@Test
	void testGlobalExceptions() throws Exception {

		final String errorMessage = "Invalid Credentials";

		// performing test
		mockMvc.perform(post("/ProcessPension").contentType(MediaType.APPLICATION_JSON).characterEncoding("UTF-8")
				.header("Authorization", "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9")
				.content(objectMapper.writeValueAsString(invalidPensionerInput)).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().is4xxClientError())
				.andExpect(jsonPath("$.message", Matchers.equalTo(errorMessage)));
	}

	@Test
	void testPensionInput_withInvalidInput() throws Exception {

		// mock processPensionService response
		when(processPensionService.getPensionDetails(ArgumentMatchers.any(), ArgumentMatchers.any()))
				.thenThrow(new NotFoundException("Details entered are incorrect"));

		// performing test
		mockMvc.perform(post("/ProcessPension").contentType(MediaType.APPLICATION_JSON).characterEncoding("utf-8")
				.header("Authorization", "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9")
				.content(objectMapper.writeValueAsString(validPensionerInput)).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().is4xxClientError())
				.andExpect(jsonPath("$.message", Matchers.equalTo("Details entered are incorrect")));
	}



	
}
