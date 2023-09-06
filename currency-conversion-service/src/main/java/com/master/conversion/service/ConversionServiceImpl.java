package com.master.conversion.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import com.master.api.dto.BankAccount;
import com.master.api.dto.CurrencyConversion;
import com.master.api.dto.CurrencyExchange;
import com.master.api.services.ConversionService;
import com.master.utility.exception.InsufficientFundsException;
import com.master.utility.exception.NoDataException;
import com.master.utility.exception.ServiceUnavailableException;

import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.core.IntervalFunction;
import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryConfig;
import io.github.resilience4j.retry.RetryRegistry;

@RestController
public class ConversionServiceImpl implements ConversionService {

	private final RestTemplate template;
	private Retry retry;

	private final Logger logger = LoggerFactory.getLogger(ConversionServiceImpl.class);

	private final String currencyExchangeUrl = "http://localhost:8000/currency-exchange";
	private final String bankAccountUrl = "http://localhost:8100/bank-account";

	private BankAccount account;
	private CurrencyExchange exchange;

	ConversionServiceImpl(RestTemplate template, RetryRegistry registry) {
		this.template = template;
		// this.retry = registry.retry("default");
		// this.retry = registry.retry("exponential");
		createJitterConfig();
	}

	@Override
	@CircuitBreaker(name = "cb", fallbackMethod = "fallback")
	public ResponseEntity<?> executeConversion(String from, String to, String username, double quantity) {

		// Request to currencyExchange has been delegated to executeSupplier method
		// If a ResourceAccessException occurs
		// Method will be invoked several times depending on the max-retry attempts
		// property value defined in application.properties

		try {
			retry.executeSupplier(() -> exchange = acquireExchange(quantity, from, to, username));
			// validate that there is an account with forwarded username
			retry.executeSupplier(() -> account = acquireAccount(username));

		} catch (ResourceAccessException e) {
			throw new ServiceUnavailableException(e.getMessage());
		}
		
		// Once currencyExchange object is obtained its exchangeRate is multiplied by
		// quantity to get the amount of to currency
		// that has to be added to the bank account
		double amountToExchange = exchange.getExchangeRate() * quantity;
		// Check if there is enough of "from" currency to execute the conversion, if not
		// custom InsufficientFundsException is thrown
		if (transactionValid(account, quantity, from)) {
			// If previous condition is true, make changes to the bank-account
			account = template.exchange(
					bankAccountUrl + "?from=" + from + "&to=" + to + "&quantityFrom=" + quantity + "&totalTo="
							+ amountToExchange + "&username=" + account.getUsername(),
					HttpMethod.PUT, null, BankAccount.class).getBody();
		} else {
			throw new InsufficientFundsException(from, quantity, account.getFromCurrency(from));
		}

		// return the end result message
		String message = "Converted " + quantity + " of " + from + " to " + amountToExchange + " of " + to;
		return ResponseEntity.ok(new CurrencyConversion(exchange, account, message));
	}

	private boolean transactionValid(BankAccount account, double conversionQuantity, String from) {
		if (account.getFromCurrency(from) - conversionQuantity >= 0)
			return true;
		return false;
	}

	private CurrencyExchange acquireExchange(double quantity, String from, String to, String username) {
		logger.info("Process of conversion of {} {} to {} for the user {} begins", quantity, from, to, username);
		CurrencyExchange exchange = null;
		try {
			exchange = template
					.getForEntity(currencyExchangeUrl + "?from=" + from + "&to=" + to, CurrencyExchange.class)
					.getBody();
		} catch (HttpServerErrorException e) {
			throw new NoDataException(from, to);
		}

		return exchange;
	}

	private BankAccount acquireAccount(String username) {
		logger.info("Retrieving bank account for user: {} ", username);
		BankAccount account = null;
		try {
			account = template.getForEntity(bankAccountUrl + "?username=" + username, BankAccount.class).getBody();
		} catch (HttpServerErrorException e) {
			throw new NoDataException(username);
		}
		return account;
	}

	private void createJitterConfig() {
		IntervalFunction randomWaitInterval = IntervalFunction.ofRandomized(2000, 0.5);
		RetryConfig jitterConfig = RetryConfig.custom().maxAttempts(3).retryExceptions(ResourceAccessException.class)
				.intervalFunction(randomWaitInterval).build();
		RetryRegistry registry = RetryRegistry.of(jitterConfig);
		this.retry = registry.retry("jitter", jitterConfig);
	}

	public ResponseEntity<?> fallback(CallNotPermittedException ex) {
		return ResponseEntity.status(429).body("Service is temporarily unavailable, please try again in 20 seconds.");
	}

}
