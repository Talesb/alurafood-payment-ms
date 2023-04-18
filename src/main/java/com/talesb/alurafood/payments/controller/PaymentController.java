package com.talesb.alurafood.payments.controller;

import java.net.URI;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.talesb.alurafood.payments.dto.PaymentDTO;
import com.talesb.alurafood.payments.service.PaymentService;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

@RestController
@RequestMapping("/payments")
public class PaymentController {

	@Autowired
	private PaymentService paymentService;

	@Autowired
	private RabbitTemplate rabbitTemplate;

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

		Message message = new Message(("Payment created, id: " + payment.getId()).getBytes());

		rabbitTemplate.send("payment.finished", message);

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

	@GetMapping("/port")
	public String getPort(@Value("${local.server.port}") String port) {
		return "Requisição respondidad pela instância executando na porta ".concat(port);
	}

	@PatchMapping("/{id}/confirm")
	@CircuitBreaker(name = "updateOrder", fallbackMethod = "paymentAuthorizedWihoutFinishingIntegration")
	public void confirmPaymen(@PathVariable @NotNull Long id) {
		paymentService.confirmPayment(id);
	}

	public void paymentAuthorizedWihoutFinishingIntegration(Long id, Exception e) {
		paymentService.updateStatus(id);
	}

}
