package com.cts.pensionerDetails.exception;


public class InvalidTokenException extends RuntimeException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2541130101094166562L;

	public InvalidTokenException(String message) {
		super(message);
	}

}