package com.master.conversion.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import com.master.api.dto.BankAccount;
import com.master.api.dto.CurrencyExchange;
import com.master.utility.exception.NoDataException;

@Component
public class FetchingService {

	private final Logger logger = LoggerFactory.getLogger(ConversionServiceImpl.class);
	private final RestTemplate template;
	private final String currencyExchangeUrl = "http://localhost:8000/currency-exchange";
	private final String bankAccountUrl = "http://localhost:8100/bank-account";
	
	public FetchingService(RestTemplate template) {
		this.template = template;
	}

	@Cacheable(cacheNames = "exchanges",key = "#from.concat('-').concat(#to)")
	public CurrencyExchange acquireExchange(double quantity, String from, String to, String username) {
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

	public BankAccount acquireAccount(String username) {
		logger.info("Retrieving bank account for user: {} ", username);
		BankAccount account = null;
		try {
			account = template.getForEntity(bankAccountUrl + "?username=" + username, BankAccount.class).getBody();
		} catch (HttpServerErrorException e) {
			throw new NoDataException(username);
		}
		return account;
	}
	
	
	public BankAccount updateAccount(String from, String to, double quantity, double amountToExchange,String username) {
		return template.exchange(
				bankAccountUrl + "?from=" + from + "&to=" + to + "&quantityFrom=" + quantity + "&totalTo="
						+ amountToExchange + "&username=" + username,
				HttpMethod.PUT, null, BankAccount.class).getBody();
	}
}
