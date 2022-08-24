package com.cts.pensionerDetails.service;

import com.cts.pensionerDetails.model.PensionerDetails;


public interface IPensionerDetailService {

	public PensionerDetails getPensionerDetailByAadhaarNumber(String token, String aadhaarNumber);

}
