package com.master.utility.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalControllerExceptionHandler {

	@ExceptionHandler(NotFoundException.class)
	public ResponseEntity<ErrorDetails> notFoundException(NotFoundException ex) {

		ErrorDetails errorModel = new ErrorDetails(HttpStatus.NOT_FOUND, ex.getMessage());

		return ResponseEntity.status(404).body(errorModel);

	}
	
	@ExceptionHandler(NoDataException.class)
	public ResponseEntity<ErrorDetails> noSuchElementException(NoDataException ex){

		ErrorDetails errorModel = new ErrorDetails(HttpStatus.NOT_FOUND, ex.getMessage());

		return ResponseEntity.status(404).body(errorModel);

	}
	
	@ExceptionHandler(InsufficientFundsException.class)
	public ResponseEntity<ErrorDetails> insufficientFunds(InsufficientFundsException ex){
		ErrorDetails errorModel = new ErrorDetails(HttpStatus.BAD_REQUEST, ex.getMessage());

		return ResponseEntity.status(400).body(errorModel);
	}
}
