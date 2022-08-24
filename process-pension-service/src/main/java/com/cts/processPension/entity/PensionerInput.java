package com.cts.processPension.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "process_pension_input")
public class PensionerInput {

	@Id
	@Pattern(regexp = "[0-9]{12}", message = "Aadhaar Number is in invalid format")
	private String aadhaarNumber;

	@Column
	private String name;

	@Column
	private Date dateOfBirth;

	@Column
	@NotNull(message = "PAN Number cannot be null")
	@NotBlank(message = "PAN Number cannot be blank")
	private String pan;

}