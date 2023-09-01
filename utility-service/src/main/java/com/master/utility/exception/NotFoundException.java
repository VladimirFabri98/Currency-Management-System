package com.master.utility.exception;

public class NotFoundException extends BusinessException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public NotFoundException() {

	}

	public NotFoundException(String message) {
		super(message);
	}
}
