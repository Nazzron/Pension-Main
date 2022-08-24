package com.cts.pensionerDetails.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;


@FeignClient(name= "AUTH-SERVICE", url= "${AUTH-SERVICE_URI:http://localhost:8081}")
public interface AuthorisationClient {


	// For validating jwt token
	@GetMapping("/api/auth/validate")
	public boolean validate(@RequestHeader(name = "Authorization") String token);

}
