package com.cts.processPension.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;



@SpringBootTest
class BankChargesRepositoryTest {

	@Autowired
	private BankChargesRepository bankChargesRepository;

	@Test
	void testFindByBankType() {

		assertEquals(500.0, bankChargesRepository.findByBankType("public").get(0).getCharges());
		assertEquals(1, bankChargesRepository.findByBankType("public").get(0).getId());
		assertEquals("public", bankChargesRepository.findByBankType("public").get(0).getBankType());
		assertEquals(550.0, bankChargesRepository.findByBankType("private").get(0).getCharges());

	}

}
