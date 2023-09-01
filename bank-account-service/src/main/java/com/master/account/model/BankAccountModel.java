package com.master.account.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "bank_account")
public class BankAccountModel {

	@Id
	private int accountId;

	private String username;
	private double eur;
	private double rsd;

	public BankAccountModel() {

	}

	public BankAccountModel(String username, double eur, double rsd) {
		this.username = username;
		this.eur = eur;
		this.rsd = rsd;
	}

	public int getAccountId() {
		return accountId;
	}

	public void setAccountId(int accountId) {
		this.accountId = accountId;
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
