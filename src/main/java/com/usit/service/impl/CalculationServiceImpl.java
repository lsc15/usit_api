package com.usit.service.impl;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.usit.domain.Calculation;
import com.usit.repository.CalculationRepository;
import com.usit.repository.MemberRepository;
import com.usit.repository.PointHistoryRepository;
import com.usit.service.CalculationService;
import com.usit.service.MemberService;
import com.usit.service.PointHistoryService;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class CalculationServiceImpl implements CalculationService {

	private static Logger logger = LoggerFactory.getLogger(CalculationServiceImpl.class);

	@Autowired
	CalculationRepository calculationRepository;

	@Autowired
	MemberRepository memberRepository;

	@Autowired
	PointHistoryRepository pointHistoryRepository;

	@Autowired
	MemberService memberService;

	@Autowired
	PointHistoryService pointHistoryService;

	@PersistenceContext
	EntityManager entityManager;

	public Calculation createCalculation(Calculation calculation) {

		return calculationRepository.save(calculation);
	}

	public Page<Calculation> readAll(Pageable pageRequest, String periodCondition, String startDate, String endDate,
			String keywordCondition, String keyword) {

		return calculationRepository.findAllBySellMemberId(pageRequest, startDate, endDate);

	}

	public Page<Calculation> readAllByToken(Pageable pageRequest, String startDate, String endDate,
			Integer sellMemberId) {

		return calculationRepository.findAllBySellMemberId(pageRequest, startDate, endDate, sellMemberId);

	}

	public Calculation modifyCalculation(Calculation calculation) {

		Calculation update = calculationRepository.findOne(calculation.getCalculationId());

		update.setStatusCd(calculation.getStatusCd());
		update.setDelayReason(calculation.getDelayReason());

		return calculationRepository.save(update);
	}

}
