package com.master.api.services;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.master.api.dto.BankAccount;

public interface AccountService {

	@GetMapping("bank-account")
	public BankAccount getAccount(@RequestParam String username);
	
	@PutMapping("bank-account")
	public BankAccount updateAccount(@RequestParam String from, @RequestParam String to, 
			@RequestParam double quantityFrom, @RequestParam double totalTo, @RequestParam String username);
	
	@PostMapping("bank-account")
	public ResponseEntity<?> createAccount(@RequestBody BankAccount account);
	
	@DeleteMapping("bank-account")
	public void deleteAccount(@RequestParam int accountId);
}
