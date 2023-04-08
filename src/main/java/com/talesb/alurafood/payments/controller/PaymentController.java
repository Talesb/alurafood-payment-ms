package com.talesb.alurafood.payments.controller;

import java.net.URI;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.talesb.alurafood.payments.dto.PaymentDTO;
import com.talesb.alurafood.payments.service.PaymentService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/payments")
public class PaymentController {

	@Autowired
	private PaymentService paymentService;

	@GetMapping
	public Page<PaymentDTO> listPayments(@PageableDefault(size = 10) Pageable pagination) {
		return paymentService.getAll(pagination);
	}

	@GetMapping("/{id}")
	public ResponseEntity<PaymentDTO> getPaymentById(@PathVariable @NotNull Long id) {
		PaymentDTO dto = paymentService.getById(id);
		return ResponseEntity.ok(dto);
	}

	@PostMapping
	public ResponseEntity<PaymentDTO> registerPayment(@RequestBody @Valid PaymentDTO dto,
			UriComponentsBuilder uriBuilder) {

		PaymentDTO payment = paymentService.createPayment(dto);
		URI paymentURI = uriBuilder.path("/payments/{id}").buildAndExpand(payment.getId()).toUri();

		return ResponseEntity.created(paymentURI).body(payment);

	}

	@PutMapping("/{id}")
	public ResponseEntity<PaymentDTO> updatePayment(@PathVariable @NotNull Long id,
			@RequestBody @Valid PaymentDTO dto) {
		PaymentDTO updated = paymentService.updatePayment(id, dto);
		return ResponseEntity.ok(updated);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<PaymentDTO> deletePayment(@PathVariable @NotNull Long id) {
		paymentService.deletePayment(id);
		return ResponseEntity.noContent().build();
	}

}
