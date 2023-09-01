package com.master.api.dto;

public class CurrencyExchange {

	private String from;
	private String to;
	private double exchangeRate;

	public CurrencyExchange() {

	}

	public CurrencyExchange(String from, String to, double exchangeRate) {
		this.from = from;
		this.to = to;
		this.exchangeRate = exchangeRate;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public double getExchangeRate() {
		return exchangeRate;
	}

	public void setExchangeRate(double exchangeRate) {
		this.exchangeRate = exchangeRate;
	}

}
