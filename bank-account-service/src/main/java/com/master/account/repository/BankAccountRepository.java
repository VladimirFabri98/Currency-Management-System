package com.master.account.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.master.account.model.BankAccountModel;

public interface BankAccountRepository extends JpaRepository<BankAccountModel, Integer>{

	Optional<BankAccountModel> findByUsername(String username);
}
