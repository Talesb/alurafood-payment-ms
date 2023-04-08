package com.talesb.alurafood.payments.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PaymentApplicationConfiguration {

	@Bean
	public ModelMapper getModelMapper() {
		return new ModelMapper();
	}

}
