package com.master.api.services;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

public interface ConversionService {

	@GetMapping("currency-conversion")
	public ResponseEntity<?> executeConversion(@RequestParam String from,
			@RequestParam String to,@RequestParam String username, @RequestParam double quantity);
}
