package com.master.conversion.service;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.ResourceAccessException;

import com.master.api.dto.BankAccount;
import com.master.api.dto.CurrencyConversion;
import com.master.api.dto.CurrencyExchange;
import com.master.api.services.ConversionService;
import com.master.utility.exception.InsufficientFundsException;
import com.master.utility.exception.ServiceUnavailableException;

import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.core.IntervalFunction;
import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryConfig;
import io.github.resilience4j.retry.RetryRegistry;

@RestController
public class ConversionServiceImpl implements ConversionService {

	private FetchingService service;
	private BankAccount account;
	private CurrencyExchange exchange;
	private Retry retry;

	ConversionServiceImpl(RetryRegistry registry, FetchingService service) {
		this.service = service;
		// this.retry = registry.retry("default");
		// this.retry = registry.retry("exponential");
		createJitterConfig();
	}

	@Override
	@CircuitBreaker(name = "cb", fallbackMethod = "fallback")
	public ResponseEntity<?> executeConversion(String from, String to, String username, double quantity) {
		// logger.info("Executing method...");
		// Request to currencyExchange has been delegated to executeSupplier method
		// If a ResourceAccessException occurs
		// Method will be invoked several times depending on the max-retry attempts
		// property value defined in application.properties
		try {
			retry.executeSupplier(() -> exchange = service.acquireExchange(quantity, from, to, username));
			// validate that there is an account with forwarded username
			retry.executeSupplier(() -> account = service.acquireAccount(username));
		} catch (Exception e) {
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
			account = service.updateAccount(from, to, quantity, amountToExchange, account.getUsername());
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
