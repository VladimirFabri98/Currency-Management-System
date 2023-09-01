package com.master.api.dto;

public class CurrencyConversion {

	private CurrencyExchange exchange;
	private BankAccount account;
	private String message;

	public CurrencyConversion() {

	}

	public CurrencyConversion(CurrencyExchange exchange, BankAccount account, String message) {
		this.exchange = exchange;
		this.account = account;
		this.message = message;
	}

	public CurrencyExchange getExchange() {
		return exchange;
	}

	public void setExchange(CurrencyExchange exchange) {
		this.exchange = exchange;
	}

	public BankAccount getAccount() {
		return account;
	}

	public void setAccount(BankAccount account) {
		this.account = account;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
