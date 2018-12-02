package com.usit.service;

import com.usit.domain.DeliveryFee;

public interface DeliveryFeeService {
	public DeliveryFee createDeliveryFee(DeliveryFee deliveryFee);
	public DeliveryFee updateDeliveryFee(DeliveryFee deliveryFee);
	public void deleteDeliveryFee(int deliveryFeeId);
	
}
