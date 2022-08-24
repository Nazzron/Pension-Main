package com.cts.processPension.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Columns;

import com.cts.processPension.model.Bank;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "pensioner_detail")
public class PensionerDetail {

	@Id
	private String name;
	
	@Column
	private Date dateOfBirth;
	
	@Column
	private String pan;
	
	@Column
	private double salary;
	
	@Column
	private double allowance;
	
	@Column
	private String pensionType;
	
	@Columns(columns = { @Column(name = "Bank_Name"), @Column(name = "Account_Number"), @Column(name = "Bank_Type") })
	private Bank bank;

}