package com.master.account.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.master.account.Mapper;
import com.master.account.model.BankAccountModel;
import com.master.account.repository.BankAccountRepository;
import com.master.api.dto.BankAccount;
import com.master.api.services.AccountService;

@RestController
public class BankAccountServiceImpl implements AccountService{

	@Autowired
	private BankAccountRepository repo;
	
	@Autowired
	private Mapper mapper;
	
	@Override
	public BankAccount getAccount(String username) {
		Optional<BankAccountModel> entity = repo.findByUsername(username);
		if(entity.isPresent()) {
			return mapper.entityToApi(entity.get());
		}else {
			throw new RuntimeException();
		}
	}

	@Override
	public ResponseEntity<?> createAccount(BankAccount account) {
		BankAccountModel savedEntity = repo.save(mapper.apiToEntity(account));
		BankAccount response = mapper.entityToApi(savedEntity);
		return ResponseEntity.status(201).body(response);
	}
	
	@Override
	public BankAccount updateAccount(String from, String to, double quantityFrom, double totalTo, String username) {
		BankAccountModel entity = repo.findByUsername(username).get();
		if(from.equalsIgnoreCase("eur") && to.equalsIgnoreCase("rsd")) {
			entity.setEur(entity.getEur() - quantityFrom);
			entity.setRsd(entity.getRsd() + totalTo);
		}else {
			entity.setEur(entity.getEur() + totalTo);
			entity.setRsd(entity.getRsd() - quantityFrom);
		}
		entity = repo.save(entity);
		
		return mapper.entityToApi(entity);
	}
	
	@Override
	public void deleteAccount(int accountId) {
		repo.deleteById(accountId);
		
	}

}
