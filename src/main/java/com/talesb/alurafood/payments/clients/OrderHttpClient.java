package com.talesb.alurafood.payments.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient("order-ms")
public interface OrderHttpClient {

	@RequestMapping(method= RequestMethod.PUT,value="/orders/{id}/paid")
	void updatePayment(@PathVariable Long id);
}
