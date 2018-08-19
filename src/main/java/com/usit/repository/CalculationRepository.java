package com.usit.repository;



import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.usit.domain.Calculation;

@Repository
public interface CalculationRepository extends JpaRepository<Calculation, Integer>{


	public Page<Calculation> findAllBySellMemberId(Pageable pageRequest,String startDate,String endDate);
	public Page<Calculation> findAllBySellMemberId(Pageable pageRequest,String startDate,String endDate,Integer memberId);
	
	
}
