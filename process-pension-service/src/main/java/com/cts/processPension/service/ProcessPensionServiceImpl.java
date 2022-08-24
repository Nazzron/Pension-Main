package com.cts.processPension.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cts.processPension.entity.PensionDetail;
import com.cts.processPension.entity.PensionerDetail;
import com.cts.processPension.entity.PensionerInput;
import com.cts.processPension.exception.NotFoundException;
import com.cts.processPension.feign.PensionerDetailsClient;
import com.cts.processPension.repository.PensionDetailRepository;
import com.cts.processPension.repository.PensionerInputRepository;
import com.cts.processPension.repository.PensionerDetailsRepository;

import lombok.extern.slf4j.Slf4j;


@Service
@Slf4j
public class ProcessPensionServiceImpl implements IProcessPensionService {

	@Autowired
	private PensionerDetailsClient pensionerDetailClient;

	@Autowired
	private PensionerInputRepository pensionerInputRepository;

	@Autowired
	private PensionDetailRepository pensionDetailRepository;

	@Autowired
	private PensionerDetailsRepository pensionerDetailsRepository;

	
	@Override
	public PensionDetail getPensionDetails(String token, PensionerInput pensionerInput) {

		PensionerDetail pensionerDetail = pensionerDetailClient.getPensionerDetailByAadhaar(token,
				pensionerInput.getAadhaarNumber());

		log.info("Details found by details microservice");

		if (checkdetails(pensionerInput, pensionerDetail)) {

			pensionerInputRepository.save(pensionerInput);

			log.info("Input saved in the database");

			pensionerDetailsRepository.save(pensionerDetail);

			log.info("Pensioner details saved in the database");

			return calculatePensionAmount(pensionerInput, pensionerDetail);

		} else {
			throw new NotFoundException("Details entered are incorrect");
		}
	}

	
	@Override
	public PensionDetail calculatePensionAmount(PensionerInput pensionerInput, PensionerDetail pensionerDetail) {
		double pensionAmount = 0;
		double bankServiceCharge = 0;

		if (pensionerDetail.getPensionType().equalsIgnoreCase("self"))
			pensionAmount = (pensionerDetail.getSalary() * 0.8 + pensionerDetail.getAllowance());
		if (pensionerDetail.getPensionType().equalsIgnoreCase("family"))
			pensionAmount = (pensionerDetail.getSalary() * 0.5 + pensionerDetail.getAllowance());

		if (pensionerDetail.getBank().getBankType().equalsIgnoreCase("public"))
			bankServiceCharge = 500;
		if (pensionerDetail.getBank().getBankType().equalsIgnoreCase("private"))
			bankServiceCharge = 550;

		PensionDetail pensionDetail = new PensionDetail(pensionerInput.getAadhaarNumber(), pensionAmount,
				bankServiceCharge, pensionerDetail.getPensionType());

		pensionDetailRepository.save(pensionDetail);

		log.info("Pension Detail (Output) saved in the database");

		return pensionDetail;
	}


	@Override
	public boolean checkdetails(PensionerInput pensionerInput, PensionerDetail pensionerDetail) {

		return (pensionerInput.getName().equalsIgnoreCase(pensionerDetail.getName())
				&& (pensionerInput.getDateOfBirth().compareTo(pensionerDetail.getDateOfBirth()) == 0)
				&& pensionerInput.getPan().equalsIgnoreCase(pensionerDetail.getPan()));
	}

}