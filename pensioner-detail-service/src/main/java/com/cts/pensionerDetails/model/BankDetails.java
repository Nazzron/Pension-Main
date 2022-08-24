package com.cts.pensionerDetails.model;

import lombok.AllArgsConstructor;
import lombok.Data;


@AllArgsConstructor
@Data
public class BankDetails {

	private String bankName;
	private long accountNumber;
	private String bankType;

}
