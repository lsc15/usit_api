package com.usit.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.usit.domain.Calculation;


public interface CalculationService {

	Calculation createCalculation(Calculation calculation);
	
	Page<Calculation> readAll(Pageable pageable,String periodCondition,String startDate,String endDate);
	
	Page<Calculation> readAllByToken(Pageable pageable,String periodCondition,String startDate,String endDate,Integer sellMemberId);
	
	Calculation modifyCalculation(Calculation calculation);
	
}