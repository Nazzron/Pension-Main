package com.cts.processPension.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.text.ParseException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.cts.processPension.entity.PensionDetail;
import com.cts.processPension.entity.PensionerDetail;
import com.cts.processPension.entity.PensionerInput;
import com.cts.processPension.exception.NotFoundException;
import com.cts.processPension.feign.PensionerDetailsClient;
import com.cts.processPension.model.Bank;
import com.cts.processPension.util.DateUtil;


@SpringBootTest
class ProcessPensionServiceImplTest {

	@Autowired
	private IProcessPensionService processPensionService;

	@MockBean
	private PensionerDetailsClient pensionerDetailClient;

	@Test
	@DisplayName("This test is for checking correct pensioner input")
	void testCheckDetailsForCorrectPensionerInput() throws ParseException {


		PensionerInput input = new PensionerInput("123456789098", "Jugraj", DateUtil.parseDate("17-01-2000"),
				"GOKU2572Q");
		Bank bank = new Bank("SBI", 456678, "public");

		PensionerDetail details = new PensionerDetail("Jugraj", DateUtil.parseDate("17-01-2000"), "GOKU2572Q",
				100000, 10000, "self", bank);

		// test
		assertTrue(processPensionService.checkdetails(input, details));
		assertEquals(456678, bank.getAccountNumber());
		assertNotNull(details);

	}

	@Test
	void testCheckDetailsForIncorrectPensionerInput() throws ParseException {

		PensionerInput input = new PensionerInput("123456789098", "Jugraj", DateUtil.parseDate("17-01-2000"),
				"ABCDE12344");
		Bank bank = new Bank("SBI", 456678, "public");
		PensionerDetail details = new PensionerDetail("Jugraj", DateUtil.parseDate("17-01-2000"), "GOKU2572Q",
				100000, 10000, "self", bank);

		// test
		assertFalse(processPensionService.checkdetails(input, details));

	}

	@Test
	void testGettingPensionDetailByPassingPensionerDetalisForSelfPensionType() throws ParseException {

		PensionerInput input = new PensionerInput("123456789098", "Jugraj", DateUtil.parseDate("17-01-2000"),
				"GOKU2572Q");

		Bank bank = new Bank("SBI", 456678, "public");
		PensionerDetail details = new PensionerDetail("Jugraj", DateUtil.parseDate("17-01-2000"), "GOKU2572Q",
				100000, 10000, "self", bank);

		PensionDetail actualDetail = processPensionService.calculatePensionAmount(input, details);

		// test
		assertEquals(90000, actualDetail.getPensionAmount());

	}

	@Test
	void testGettingPensionDetailByPassingPensionerDetalisForFamilyPensionType() throws ParseException {

		PensionerInput input = new PensionerInput("123456789098", "Jugraj", DateUtil.parseDate("17-01-2000"),
				"GOKU2572Q");

		Bank bank = new Bank("SBI", 456678, "public");
		PensionerDetail details = new PensionerDetail("Jugraj", DateUtil.parseDate("17-01-2000"), "GOKU2572Q",
				100000, 10000, "family", bank);

		PensionDetail actualDetail = processPensionService.calculatePensionAmount(input, details);

		// test
		assertEquals(60000, actualDetail.getPensionAmount());

	}

	@Test
	void testGettingPensionDetailByPassingPensionerDetalisForPublicBanktype() throws ParseException {

		PensionerInput input = new PensionerInput("123456789098", "Jugraj", DateUtil.parseDate("17-01-2000"),
				"GOKU2572Q");

		Bank bank = new Bank("SBI", 123456, "public");
		PensionerDetail details = new PensionerDetail("Jugraj", DateUtil.parseDate("17-01-2000"), "GOKU2572Q",
				100000, 10000, "family", bank);

		PensionDetail actualDetail = processPensionService.calculatePensionAmount(input, details);

		// test
		assertEquals(60000, actualDetail.getPensionAmount());

	}



	@Test
	void testGetPensionDetails_forFamily() throws ParseException {

		PensionerInput pensionerInput = new PensionerInput("123456789098", "Jugraj", DateUtil.parseDate("17-01-2000"),
				"GOKU2572Q");

		Bank bank = new Bank("SBI", 456678, "public");

		PensionerDetail details_family = new PensionerDetail("Jugraj", DateUtil.parseDate("17-01-2000"), "GOKU2572Q",
				100000, 10000, "family", bank);

		// mock the feign client
		when(pensionerDetailClient.getPensionerDetailByAadhaar("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9",
				pensionerInput.getAadhaarNumber())).thenReturn(details_family);

		// get the actual result
		PensionDetail pensionDetailFamily = processPensionService
				.getPensionDetails("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9", pensionerInput);

		// test
		assertEquals(60000, pensionDetailFamily.getPensionAmount());
		assertNotNull(pensionDetailFamily);

	}

	
		@Test
	void testCheckDetails_incorrectPensionerInput() throws ParseException {

		PensionerInput pensionerInput = new PensionerInput("123456789098", "Jugraj", DateUtil.parseDate("17-01-2000"),
				"GOKU2572Q");

		Bank bank = new Bank("SBI", 456678, "public");

		PensionerDetail details = new PensionerDetail("Jugraj", DateUtil.parseDate("17-01-2000"), "ASDFG3456", 100000,
				10000, "self", bank);

		// mock the feign client
		when(pensionerDetailClient.getPensionerDetailByAadhaar("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9",
				pensionerInput.getAadhaarNumber())).thenReturn(details);

		NotFoundException notFoundException = assertThrows(NotFoundException.class,
				() -> processPensionService.getPensionDetails("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9", pensionerInput));

		// test
		assertEquals("Details entered are incorrect", notFoundException.getMessage());
		assertNotNull(notFoundException);

	}

}