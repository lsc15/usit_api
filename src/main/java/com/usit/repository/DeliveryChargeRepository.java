package com.usit.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.usit.domain.DeliveryCharge;
@Repository
public interface DeliveryChargeRepository extends JpaRepository<DeliveryCharge, String> {
	public List<DeliveryCharge> findAllByPostCode(String postCode);
}
