package com.cts.processPension.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.text.ParseException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.cts.processPension.entity.PensionerInput;
import com.cts.processPension.util.DateUtil;


@SpringBootTest
class PensionerInputRepositoryTests {

	@Autowired
	private PensionerInputRepository pensionerInputRepository;

	@Test
	@DisplayName("This method is responsible to test save() for pensioner input details")
	void testSaveForPensionerInputDetails() throws ParseException {

		PensionerInput pi = new PensionerInput("300546467895", "Jugraj", DateUtil.parseDate("17-01-2000"),
				"GOKU2572Q");

		PensionerInput savedDetails = pensionerInputRepository.save(pi);
	
		assertEquals(savedDetails.getAadhaarNumber(), pi.getAadhaarNumber());
		assertNotNull(savedDetails);

	}
}
