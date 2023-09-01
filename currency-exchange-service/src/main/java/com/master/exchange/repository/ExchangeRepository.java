package com.master.exchange.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.master.exchange.model.CurrencyExchangeModel;

public interface ExchangeRepository extends JpaRepository<CurrencyExchangeModel,Integer> {

	Optional<CurrencyExchangeModel> findByFromAndTo(String from, String to);
}
