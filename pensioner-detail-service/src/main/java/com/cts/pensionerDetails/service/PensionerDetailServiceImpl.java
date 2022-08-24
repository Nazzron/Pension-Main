package com.cts.pensionerDetails.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.cts.pensionerDetails.exception.NotFoundException;
import com.cts.pensionerDetails.model.BankDetails;
import com.cts.pensionerDetails.model.PensionerDetails;
import com.cts.pensionerDetails.util.DateUtil;

import lombok.extern.slf4j.Slf4j;


@Service
@Slf4j
public class PensionerDetailServiceImpl implements IPensionerDetailService {

	private PensionerDetails pensionerDetails;

	@Value("${errorMessage}")
	private String AADHAAR_NUMBER_NOT_FOUND;

	
	// Loads pensioner details from the details.csv
	public PensionerDetails getPensionerDetailByAadhaarNumber(String token, String aadhaarNumber) {

		String line = "";
		BufferedReader br = new BufferedReader(
				new InputStreamReader(this.getClass().getResourceAsStream("/details.csv")));
		try {
			while ((line = br.readLine()) != null)
			{
				String[] person = line.split(",");

				if (aadhaarNumber.contentEquals(person[0])) {
					log.info("Pensioner Details Found");
					pensionerDetails = new PensionerDetails(person[1], DateUtil.parseDate(person[2]), person[3],
							Double.parseDouble(person[4]), Double.parseDouble(person[5]), person[6],
							new BankDetails(person[7], Long.parseLong(person[8]), person[9]));

					return pensionerDetails;
				}
			}
		} catch (NumberFormatException | IOException | ParseException e) {
			throw new NotFoundException(AADHAAR_NUMBER_NOT_FOUND);
		}
		throw new NotFoundException(AADHAAR_NUMBER_NOT_FOUND);
	}

}
