package com.master.utility.exception;

public class ServiceUnavailableException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ServiceUnavailableException() {

	}

	public ServiceUnavailableException(String message) {
		super(message);
	}
}
