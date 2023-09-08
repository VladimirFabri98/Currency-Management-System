package com.master.exchange.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.master.api.dto.CurrencyExchange;
import com.master.api.services.ExchangeService;
import com.master.exchange.Mapper;
import com.master.exchange.model.CurrencyExchangeModel;
import com.master.exchange.repository.ExchangeRepository;

@RestController
public class CurrencyExchangeServiceImpl implements ExchangeService {

	@Autowired
	private ExchangeRepository repo;

	@Autowired
	private Mapper mapper;

	@Override
	public CurrencyExchange getExchangeByFromAndTo(String from, String to) {
		Optional<CurrencyExchangeModel> entity = repo.findByFromAndTo(from, to);
		if(entity.isPresent()) {
			return mapper.entityToApi(entity.get());
		}else {
			//This exception will be wrapped inside of a HttpServerErrorException
			throw new RuntimeException();
		}
		
	}

	@Override
	public ResponseEntity<?> createExchange(CurrencyExchange exchange) {
		CurrencyExchangeModel savedEntity = repo.save(mapper.apiToEntity(exchange));
		return ResponseEntity.status(201).body(savedEntity);
	}

	@Override
	@CacheEvict(cacheNames="exchanges")
	public void deleteExchange(int exchangeId) {
		repo.deleteById(exchangeId);

	}

}
