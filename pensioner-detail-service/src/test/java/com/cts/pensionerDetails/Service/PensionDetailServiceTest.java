package com.cts.pensionerDetails.Service;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.text.ParseException;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import com.cts.pensionerDetails.exception.NotFoundException;
import com.cts.pensionerDetails.model.BankDetails;
import com.cts.pensionerDetails.model.PensionerDetails;
import com.cts.pensionerDetails.service.IPensionerDetailService;
import com.cts.pensionerDetails.util.DateUtil;


@SpringBootTest
class PensionDetailServiceTest {

	@Autowired
	IPensionerDetailService pds;

	@Value("${errorMessage}")
	private String AADHAAR_NUMBER_NOT_FOUND;

	@Test
	void testNotNullPensionDetailServiceObject() {

		assertNotNull(pds);

	}

	@Test
	void testCorrectDetailsReturnedFromServiceWithCorrectAadharNumber()
			throws IOException, NotFoundException, NumberFormatException,
			com.cts.pensionerDetails.exception.NotFoundException, ParseException, NullPointerException {

		final String aadhaarNumber = "123456789098";

		PensionerDetails pensionerDetail = new PensionerDetails("Jugraj", DateUtil.parseDate("17-01-1978"),
				"GOKU2572Q", 98000, 12000, "self", new BankDetails("SBI", 12345678, "private"));
		assertEquals(
				pds.getPensionerDetailByAadhaarNumber("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9", aadhaarNumber).getPan(),
				pensionerDetail.getPan());
		assertEquals(pds.getPensionerDetailByAadhaarNumber("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9", aadhaarNumber)
				.getBank().getAccountNumber(), pensionerDetail.getBank().getAccountNumber());
	}

	@Test
	void testForIncorrectAadharNumber() {
		final String aadhaarNumber = "112233445566";

		NotFoundException exception = assertThrows(NotFoundException.class,
				() -> pds.getPensionerDetailByAadhaarNumber("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9", aadhaarNumber));
		assertEquals(exception.getMessage(), AADHAAR_NUMBER_NOT_FOUND);
		assertNotNull(exception);
	}
}
