package com.talesb.alurafood.payments.service;

import javax.persistence.EntityNotFoundException;

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

}
