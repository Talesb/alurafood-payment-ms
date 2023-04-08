package com.talesb.alurafood.payments.dto;

import java.math.BigDecimal;

import com.talesb.alurafood.payments.model.Status;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentDTO {

	private Long id;
	private BigDecimal value;
	private String name;
	private String number;
	private String expiration;
	private String code;
	private Status status;
	private Long periodId;
	private Long paymentMethodId;

}
