package com.master.conversion.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import com.master.api.dto.BankAccount;
import com.master.api.dto.CurrencyConversion;
import com.master.api.dto.CurrencyExchange;
import com.master.api.services.ConversionService;
import com.master.utility.exception.InsufficientFundsException;
import com.master.utility.exception.NoDataException;

@RestController
public class ConversionServiceImpl implements ConversionService {

	@Autowired
	private RestTemplate template;

	private final String currencyExchangeUrl = "http://localhost:8000/currency-exchange";
	private final String bankAccountUrl = "http://localhost:8100/bank-account";
	private BankAccount account;
	private CurrencyExchange exchange;

	@Override
	public ResponseEntity<?> executeConversion(String from, String to, String username, double quantity) {
		
		//getExchange by using from and to
		//Wrap this get request in try catch block in case HttpServerErrorException gets thrown
		//HttpServerErrorException will get thrown if no exchange is found in the database
		try {
			exchange = template
					.getForEntity(currencyExchangeUrl + "?from=" + from + "&to=" + to, CurrencyExchange.class)
					.getBody();
		} catch (HttpServerErrorException ex) {
			//If caught throw a custom NoDataException
			throw new NoDataException(from,to);
		}

		// Once currencyExchange object is obtained its exchangeRate is multiplied by quantity to get the amount of to currency
		// that has to be added to the bank account 
		double amountToExchange = exchange.getExchangeRate() * quantity;
		// validate that there is an account with forwarded username
		try {
			account = template.getForEntity(bankAccountUrl + "?username=" + username, BankAccount.class).getBody();
		//Reasoning behind this try-catch block is identical to the last one
		} catch (HttpServerErrorException ex) {
			throw new NoDataException(username);
		}
		
		//Check if there is enough of "from" currency to execute the conversion, if not custom InsufficientFundsException is thrown
		if(transactionValid(account, quantity, from)) {
		//If previous condition is true, make changes to the bank-account
			account = template.exchange(
					bankAccountUrl + "?from=" + from + "&to=" + to + "&quantityFrom=" + quantity + "&totalTo="
							+ amountToExchange + "&username=" + account.getUsername(),
					HttpMethod.PUT, null, BankAccount.class).getBody();
		}else {
			throw new InsufficientFundsException(from,quantity,account.getFromCurrency(from));
		}
		
		// return the end result message
		String message = "Converted " + quantity + " of " + from + " to " + amountToExchange + " of " + to;
		return ResponseEntity.ok(new CurrencyConversion(exchange, account, message));
	}
	
	private boolean transactionValid(BankAccount account ,double conversionQuantity, String from) {
		if(account.getFromCurrency(from) - conversionQuantity >=0) return true;
		return false;
	}

}
