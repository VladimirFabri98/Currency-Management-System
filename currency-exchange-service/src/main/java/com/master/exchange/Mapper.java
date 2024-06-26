package com.master.exchange;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.master.api.dto.CurrencyExchange;
import com.master.exchange.model.CurrencyExchangeModel;

@Component
public class Mapper {
	
	public Mapper() {
		
	}

	public List<CurrencyExchange> entitiesToApi(List<CurrencyExchangeModel> entities){
		List<CurrencyExchange> exchanges = new ArrayList<CurrencyExchange>();
		if(!entities.isEmpty()) {
			for(CurrencyExchangeModel entity: entities) {
				exchanges.add(new CurrencyExchange(entity.getFrom()
						,entity.getTo(),entity.getExchangeRate()));
			}
		}
		return exchanges;
	}
	
	public CurrencyExchange entityToApi(CurrencyExchangeModel entity) {
		return new CurrencyExchange(entity.getFrom(),entity.getTo(),entity.getExchangeRate());
	}
	
	public CurrencyExchangeModel apiToEntity(CurrencyExchange api) {
		return new CurrencyExchangeModel(api.getFrom(),api.getTo(),api.getExchangeRate());
	}
}
