package com.usit.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.usit.domain.DeliveryFee;

public interface DeliveryFeeRepository extends JpaRepository<DeliveryFee, Integer> {
}
