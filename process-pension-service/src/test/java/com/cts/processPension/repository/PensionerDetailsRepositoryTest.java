package com.cts.processPension.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.text.ParseException;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.cts.processPension.entity.PensionerDetail;
import com.cts.processPension.model.Bank;
import com.cts.processPension.util.DateUtil;




@SpringBootTest
class PensionerDetailsRepositoryTest {

	@Autowired
	private PensionerDetailsRepository pensionerDetailsRepository;

	@Test
	void testsaveforPensionerDetails() throws ParseException {

		PensionerDetail pd = new PensionerDetail("Jugraj", DateUtil.parseDate("17-01-2000"), "GOKU2572Q", 50000,
				10000, "self", new Bank("SBI", 12345678, "public"));

		PensionerDetail savedDetails = pensionerDetailsRepository.save(pd);

		assertEquals(savedDetails.getPan(), pd.getPan());
		assertNotNull(savedDetails);

	}
}
