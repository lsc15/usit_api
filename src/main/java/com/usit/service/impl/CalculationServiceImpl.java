package com.usit.service.impl;


import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.usit.app.spring.util.DateUtil;
import com.usit.app.spring.util.UsitCodeConstants;
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

	
	@Autowired
    JdbcTemplate jdbcTemplate;
	
	@PersistenceContext
	EntityManager entityManager;

	
    //ORM transaction 문제로jdbcTemplagte 사용
    @Transactional(propagation = Propagation.REQUIRED)
	public int createCalculation(Calculation calculation) {

		return jdbcTemplate.update("INSERT INTO calculation "
				+ "(type_cd, status_cd, purchase_confirm_date, calculation_due_date, amount, order_item_id, sell_member_id , reg_id, reg_date )"
				+ "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)", 
				calculation.getTypeCd(), calculation.getStatusCd(), calculation.getPurchaseConfirmDate(), calculation.getCalculationDueDate(), calculation.getAmount(), calculation.getOrderItemId(),
				calculation.getSellMemberId(), calculation.getRegId(), calculation.getRegDate());
      
	}

	public Page<Calculation> readAll(Pageable pageRequest, String periodCondition, String startDate, String endDate) {
		
		
//		int syear = Integer.parseInt(startDate.substring(0, 4));
//		int smonth = Integer.parseInt(startDate.substring(4, 6));
//		int sday = Integer.parseInt(startDate.substring(6, 8));
//		int eyear = Integer.parseInt(endDate.substring(0, 4));
//		int emonth = Integer.parseInt(endDate.substring(4, 6));
//		int eday = Integer.parseInt(endDate.substring(6, 8));
//		LocalDateTime s = LocalDateTime.of(syear, smonth, sday, 00, 00, 00);
//		LocalDateTime e = LocalDateTime.of(eyear, emonth, eday, 23, 59, 59);
		
		Page<Calculation> page = null;
	    //구매확정일
		if(UsitCodeConstants.PERIOD_CONDITION_PURCHASE.equals(periodCondition)) {
			page = calculationRepository.findAllByPurchaseConfirmDateBetween(pageRequest, startDate, endDate);
		//구매예정일	
		}else if(UsitCodeConstants.PERIOD_CONDITION_DUE_DATE.equals(periodCondition)) {
			page = calculationRepository.findAllByCalculationDueDateBetween(pageRequest, startDate, endDate);
			
		//정산완료일
		}else if(UsitCodeConstants.PERIOD_CONDITION_COMPLETE_DATE.equals(periodCondition)) {
		
			page = calculationRepository.findAllByCalculationDateBetween(pageRequest, startDate, endDate);
		}
		
		
		return page;

	}

	public Page<Calculation> readAllByToken(Pageable pageRequest, String periodCondition, String startDate, String endDate, Integer sellMemberId) {

//		int syear = Integer.parseInt(startDate.substring(0, 4));
//		int smonth = Integer.parseInt(startDate.substring(4, 6));
//		int sday = Integer.parseInt(startDate.substring(6, 8));
//		int eyear = Integer.parseInt(endDate.substring(0, 4));
//		int emonth = Integer.parseInt(endDate.substring(4, 6));
//		int eday = Integer.parseInt(endDate.substring(6, 8));
//		LocalDateTime s = LocalDateTime.of(syear, smonth, sday, 00, 00, 00);
//		LocalDateTime e = LocalDateTime.of(eyear, emonth, eday, 23, 59, 59);
		
		Page<Calculation> page = null;
	    //구매확정일
		if(UsitCodeConstants.PERIOD_CONDITION_PURCHASE.equals(periodCondition)) {
			page = calculationRepository.findAllBySellMemberIdAndPurchaseConfirmDateBetween(pageRequest, sellMemberId, startDate, endDate);
		//정산예정일	
		}else if(UsitCodeConstants.PERIOD_CONDITION_DUE_DATE.equals(periodCondition)) {
			page = calculationRepository.findAllBySellMemberIdAndCalculationDueDateBetween(pageRequest, sellMemberId, startDate, endDate);
			
		//정산완료일
		}else if(UsitCodeConstants.PERIOD_CONDITION_COMPLETE_DATE.equals(periodCondition)) {
		
			page = calculationRepository.findAllBySellMemberIdAndCalculationDateBetween(pageRequest, sellMemberId, startDate, endDate);
		}
		
		
		return page;
		

	}

	public Calculation modifyCalculation(Calculation calculation) {

		Calculation update = calculationRepository.findOne(calculation.getCalculationId());

		update.setStatusCd(calculation.getStatusCd());
		
		if(UsitCodeConstants.CACULATION_STATUS_CD_COMPLETE.equals(calculation.getStatusCd())) {
			update.setCalculationDate(DateUtil.getCurrDate());
		}else {
			update.setCalculationDate("");
		}
		
		update.setDelayReason(calculation.getDelayReason());

		
		
		return calculationRepository.save(update);
	}

}
