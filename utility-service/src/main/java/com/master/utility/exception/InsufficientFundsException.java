package com.master.utility.exception;

public class InsufficientFundsException extends BusinessException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public InsufficientFundsException() {

	}

	public InsufficientFundsException(String from, double exchangeQuantity, double availableFunds) {
		super("You have insufficient funds for the conversion of " + exchangeQuantity + " " + from.toUpperCase() + 
				" , you have " + availableFunds + " " + from.toUpperCase() + " at your disposal.");
	}
}
