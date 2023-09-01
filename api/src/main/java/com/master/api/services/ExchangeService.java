package com.master.api.services;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.master.api.dto.CurrencyExchange;

public interface ExchangeService {

	@GetMapping("currency-exchange")
	public CurrencyExchange getExchangeByFromAndTo(@RequestParam(defaultValue = "eur") String from,
			@RequestParam(defaultValue = "rsd") String to);
	
	@PostMapping("currency-exchange")
	public ResponseEntity<?> createExchange(@RequestBody CurrencyExchange exchange);
	
	@DeleteMapping("currency-exchange")
	public void deleteExchange(@RequestParam int exchangeId);
}
