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
	
	@ExceptionHandler(ServiceUnavailableException.class)
	public ResponseEntity<ErrorDetails> serviceUnavailable(ServiceUnavailableException ex){
		String[] messageArray = ex.getMessage().split(" ");
		String[] actualServiceUrlArray = messageArray[6].substring(1, messageArray[6].length() - 2).split("/");
		// [0]http: [1]"" [2]localhost:8000 [3]currency-exchange or [3]bank-account
		String actualServiceUrl = messageArray[6].substring(1, messageArray[6].length() - 2);
		// http://localhost:8000/currency-exchange or http://localhost:8100/bank-account
		String serviceName = actualServiceUrlArray[3];
		String message = "Service: " + serviceName + " at: " + actualServiceUrl + " is currently unavailable!";
		ErrorDetails errorModel = new ErrorDetails(HttpStatus.SERVICE_UNAVAILABLE, message);
		return ResponseEntity.status(503).body(errorModel);
	}
	
}
