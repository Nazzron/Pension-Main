package com.cts.processPension.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.text.ParseException;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.cts.processPension.entity.PensionDetail;


@SpringBootTest
class PensionDetailRepositoryTest {

	@Autowired
	private PensionDetailRepository pensionDetailRepository;

	@Test
	void testSaveForPensionDetail() throws ParseException {

		PensionDetail pd = new PensionDetail("123456789098", 26000, 500, "self");

		PensionDetail savedDetails = pensionDetailRepository.save(pd);

		assertEquals(savedDetails.getAadhaarNumber(), pd.getAadhaarNumber());
		assertNotNull(savedDetails);


	}

}
