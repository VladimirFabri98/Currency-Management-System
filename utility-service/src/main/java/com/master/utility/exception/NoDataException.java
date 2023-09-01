package com.master.utility.exception;

public class NoDataException extends BusinessException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public NoDataException() {

	}

	public NoDataException(String from, String to) {
		super("Exchange rate for pair [" + from + " to " + to + "] does not exist");
	}
	
	public NoDataException(String username) {
		super("Bank account with following username: " + username + " could not be found.");
	}

}
