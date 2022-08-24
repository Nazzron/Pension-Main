package com.cts.processPension.service;

import com.cts.processPension.entity.PensionDetail;
import com.cts.processPension.entity.PensionerDetail;
import com.cts.processPension.entity.PensionerInput;


public interface IProcessPensionService {

	
	
	public PensionDetail getPensionDetails(String token, PensionerInput pensionerInput);
	
	public PensionDetail calculatePensionAmount(PensionerInput pensionerInput, PensionerDetail pensionDetail);

	public boolean checkdetails(PensionerInput pensionerInput, PensionerDetail pensionerDetail);

}
