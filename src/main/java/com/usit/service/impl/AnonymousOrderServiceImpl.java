package com.usit.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.usit.app.spring.exception.FrameworkException;
import com.usit.domain.AnonymousOrder;
import com.usit.repository.AnonymousOrderRepository;
import com.usit.repository.MemberRepository;
import com.usit.service.AnonymousOrderService;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class AnonymousOrderServiceImpl implements AnonymousOrderService{

	private static Logger LOGGER = LoggerFactory.getLogger(AnonymousOrderServiceImpl.class);



	@Autowired
	AnonymousOrderService anonymousOrderService;
	
	@Autowired
	MemberRepository memberRepository;
	
	@Autowired
	AnonymousOrderRepository anonymousOrderRepository;



	
//	public AnonymousOrder getAnonymousOrder(Long memberId) {
//		
//		return anonymousOrderRepository.findByMemberIdOrderByAnonymousOrderIdDesc(memberId);
//
//	}
	

	public AnonymousOrder createAnonymousOrder(AnonymousOrder anonymousOrder) {
		
		return anonymousOrderRepository.save(anonymousOrder);
	}
	
	

	public AnonymousOrder updateAnonymousOrder(AnonymousOrder anonymousOrder,int anonymousOrderId,int memberId) {
		AnonymousOrder updateAnonymousOrder = anonymousOrderRepository.findOne(anonymousOrderId);
		
		if(updateAnonymousOrder==null) {
			LOGGER.warn("해당 장바구니가 없습니다.");
			throw new FrameworkException("-1001", "존재하지 않는 주문입니다"); // 오류 리턴 예시
		}else{
			updateAnonymousOrder.setEmail(anonymousOrder.getEmail());
			updateAnonymousOrder.setName(anonymousOrder.getName());
			updateAnonymousOrder.setOrderId(anonymousOrder.getOrderId());
			updateAnonymousOrder.setPassword(anonymousOrder.getPassword());
			updateAnonymousOrder.setPhone(anonymousOrder.getPhone());
			updateAnonymousOrder.setUserIp(anonymousOrder.getUserIp());
		
		return anonymousOrderRepository.save(updateAnonymousOrder);
			
		}
		
		
		
	}

	
	//비회원주문 삭제
		public void deleteAnonymousOrder(int anonymousOrderId) {
			AnonymousOrder anonymousOrder = anonymousOrderRepository.findOne(anonymousOrderId);
			if(anonymousOrder==null) {
				LOGGER.warn("해당 주문이 없습니다.");
				throw new FrameworkException("-1001", "존재하지 않는 주문입니다"); // 오류 리턴 예시
			}else{
				anonymousOrderRepository.delete(anonymousOrderId);;
			}
			
			
		}
	

	
}
