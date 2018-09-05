package com.usit.repository;



import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.usit.domain.Calculation;

@Repository
public interface CalculationRepository extends JpaRepository<Calculation, Integer>{


//	public Page<Calculation> findAllBySellMemberId(Pageable pageRequest,String startDate,String endDate);
	
	public Page<Calculation> findAllByPurchaseConfirmDateBetween(Pageable pageRequest,LocalDateTime startDate,LocalDateTime endDate);
	public Page<Calculation> findAllByCalculationDueDateBetween(Pageable pageRequest,LocalDateTime startDate,LocalDateTime endDate);
	public Page<Calculation> findAllByCalculationDateBetween(Pageable pageRequest,LocalDateTime startDate,LocalDateTime endDate);
	
	public Page<Calculation> findAllBySellMemberIdAndPurchaseConfirmDateBetween(Pageable pageRequest,int sellMemberId,LocalDateTime startDate,LocalDateTime endDate);
	public Page<Calculation> findAllBySellMemberIdAndCalculationDueDateBetween(Pageable pageRequest,int sellMemberId,LocalDateTime startDate,LocalDateTime endDate);
	public Page<Calculation> findAllBySellMemberIdAndCalculationDateBetween(Pageable pageRequest,int sellMemberId,LocalDateTime startDate,LocalDateTime endDate);
	
//	public Page<Calculation> findAllBySellMemberId(Pageable pageRequest,String startDate,String endDate,Integer memberId);
	
	
}
