package com.cts.processPension.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

import com.cts.processPension.entity.PensionerDetail;


@FeignClient(name= "PENSIONER-DETAIL-SERVICE", url= "${PENSIONER-DETAIL-SERVICE_URI:http://localhost:8083}")
public interface PensionerDetailsClient {
	
	@GetMapping("/api/pensioner-detail/PensionerDetailByAadhaar/{aadhaarNumber}")
	public PensionerDetail getPensionerDetailByAadhaar(@RequestHeader(name = "Authorization") String token,
			@PathVariable String aadhaarNumber);
}