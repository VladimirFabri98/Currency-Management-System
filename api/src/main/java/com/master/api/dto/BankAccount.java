package com.master.api.dto;

public class BankAccount {

	private String username;
	private double eur;
	private double rsd;

	public BankAccount() {

	}

	public BankAccount(String username, double eur, double rsd) {
		this.username = username;
		this.eur = eur;
		this.rsd = rsd;
	}
	
	public double getFromCurrency(String from) {
		if(from.equalsIgnoreCase("eur")) {
			return getEur();
		}
		return getRsd();
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public double getEur() {
		return eur;
	}

	public void setEur(double eur) {
		this.eur = eur;
	}

	public double getRsd() {
		return rsd;
	}

	public void setRsd(double rsd) {
		this.rsd = rsd;
	}

}
