package com.talesb.alurafood.payments.service;

import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import com.talesb.alurafood.payments.clients.OrderHttpClient;
import com.talesb.alurafood.payments.dto.PaymentDTO;
import com.talesb.alurafood.payments.model.Payment;
import com.talesb.alurafood.payments.model.Status;
import com.talesb.alurafood.payments.repository.PaymentRepository;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {

	@Autowired
	private PaymentRepository paymentRepository;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private OrderHttpClient httpClient;

	public Page<PaymentDTO> getAll(Pageable pagination) {
		return paymentRepository.findAll(pagination).map(p -> modelMapper.map(p, PaymentDTO.class));
	}

	public PaymentDTO getById(Long id) {
		Payment payment = this.paymentRepository.findById(id).orElseThrow(() -> new EntityNotFoundException());
		return modelMapper.map(payment, PaymentDTO.class);
	}

	public PaymentDTO createPayment(PaymentDTO dto) {
		Payment payment = modelMapper.map(dto, Payment.class);
		payment.setStatus(Status.CRIADO);
		paymentRepository.save(payment);
		return modelMapper.map(payment, PaymentDTO.class);
	}

	public PaymentDTO updatePayment(Long id, PaymentDTO dto) {
		Payment payment = modelMapper.map(dto, Payment.class);
		payment.setId(id);
		payment = paymentRepository.save(payment);
		return modelMapper.map(payment, PaymentDTO.class);
	}

	public void deletePayment(Long id) {
		this.paymentRepository.deleteById(id);
	}

	public void confirmPayment(Long id) {
		Optional<Payment> payment = paymentRepository.findById(id);

		if (!payment.isPresent()) {
			throw new EntityNotFoundException();
		}

		payment.get().setStatus(Status.CONFIRMADO);
		paymentRepository.save(payment.get());

		httpClient.updatePayment(payment.get().getOrderId());
	}

	public void updateStatus(Long id) {

		Optional<Payment> payment = paymentRepository.findById(id);

		if (!payment.isPresent()) {
			throw new EntityNotFoundException();
		}

		payment.get().setStatus(Status.CONFIRMADO_SEM_INTEGRACAO);
		paymentRepository.save(payment.get());

	}

}
