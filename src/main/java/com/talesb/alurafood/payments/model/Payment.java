package com.talesb.alurafood.payments.model;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "payment")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Payment {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull
	@Positive
	private BigDecimal value;

	@NotBlank
	@Size(max = 100)
	private String name;

	@NotBlank
	@Size(max = 19)
	private String number;

	@NotBlank
	@Size(max = 7)
	private String expiration;

	@NotBlank
	@Size(max = 3, min = 3)
	private String code;

	@NotNull
	@Enumerated(EnumType.STRING)
	private Status status;

	@NotNull
	@Column(name="order_id")
	private Long orderId;

	@NotNull
	@Column(name="payment_method_id")
	private Long paymentMethodId;

}
