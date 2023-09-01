package com.master.exchange.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "currency_exchange")
public class CurrencyExchangeModel {

	@Id
	private int exchangeId;
	@Column(name = "currency_from")
	private String from;

	@Column(name = "currency_to")
	private String to;

	private double exchangeRate;

	public CurrencyExchangeModel() {

	}

	public CurrencyExchangeModel(String from, String to, double exchangeRate) {
		this.from = from;
		this.to = to;
		this.exchangeRate = exchangeRate;
	}

	public int getExchangeId() {
		return exchangeId;
	}

	public void setExchangeId(int exchangeId) {
		this.exchangeId = exchangeId;
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
