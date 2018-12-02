package com.usit.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.usit.domain.DeliveryFee;
import com.usit.repository.DeliveryFeeRepository;
import com.usit.service.DeliveryFeeService;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class DeliveryFeeServiceImpl implements DeliveryFeeService {
	
	@Autowired DeliveryFeeRepository deliveryFeeRepository;

	@Override
	public DeliveryFee createDeliveryFee(DeliveryFee deliveryFee) {
		return deliveryFeeRepository.save(deliveryFee);
	}

	
	public DeliveryFee updateDeliveryFee(DeliveryFee deliveryFee) {
		DeliveryFee updateDeliveryFee = deliveryFeeRepository.findOne(deliveryFee.getDeliveryFeeId());
		updateDeliveryFee.setAmount(deliveryFee.getAmount());
		updateDeliveryFee.setFreeCondition(deliveryFee.getFreeCondition());
		updateDeliveryFee.setOrderItemTotalAmount(deliveryFee.getOrderItemTotalAmount());
		
		DeliveryFee result = deliveryFeeRepository.save(updateDeliveryFee);
		return result;
	}
	
	public void deleteDeliveryFee(int deliveryFeeId) {
		deliveryFeeRepository.delete(deliveryFeeId);
	}

}
