package com.master.account;

import org.springframework.stereotype.Component;

import com.master.account.model.BankAccountModel;
import com.master.api.dto.BankAccount;

@Component
public class Mapper {

	public Mapper() {
		
	}
	
	public BankAccount entityToApi(BankAccountModel entity) {
		try {
			return new BankAccount(entity.getUsername(),entity.getEur(),entity.getRsd());
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}
	
	public BankAccountModel apiToEntity(BankAccount api) {
		try {
			return new BankAccountModel(api.getUsername(),api.getEur(),api.getRsd());
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}
}
