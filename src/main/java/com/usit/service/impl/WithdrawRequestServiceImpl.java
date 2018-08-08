package com.usit.service.impl;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.usit.app.spring.exception.FrameworkException;
import com.usit.app.spring.util.AES256Util;
import com.usit.app.spring.util.UsitCodeConstants;
import com.usit.domain.Member;
import com.usit.domain.PointHistory;
import com.usit.domain.WithdrawRequest;
import com.usit.repository.MemberRepository;
import com.usit.repository.PointHistoryRepository;
import com.usit.repository.WithdrawRequestRepository;
import com.usit.service.MemberService;
import com.usit.service.PointHistoryService;
import com.usit.service.WithDrawRequestService;
import com.usit.util.TimeUtil;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class WithdrawRequestServiceImpl implements WithDrawRequestService{

	private static Logger logger = LoggerFactory.getLogger(WithdrawRequestServiceImpl.class);



	@Autowired
	WithdrawRequestRepository withdrawRequestRepository;
	
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
	
	
	public WithdrawRequest createWithdrawRequest(WithdrawRequest withdrawRequest) throws UnsupportedEncodingException, NoSuchAlgorithmException, GeneralSecurityException {
		
		//포인트 차감
		Member member = memberService.getMemberByMemeberId(withdrawRequest.getMemberId());
		int present = member.getTotalPoint() - withdrawRequest.getAmount();
		member.setTotalPoint(present);
		member.setWithdrawablePoint(member.getWithdrawablePoint() - withdrawRequest.getAmount());
		memberRepository.save(member);
		
		//포인트 이력
		PointHistory pointHistory  = new PointHistory();
		pointHistory.setMemberId(withdrawRequest.getMemberId());
		pointHistory.setPointTypeCd(UsitCodeConstants.POINT_TYPE_CD_WITHDRAW);
		pointHistory.setAddPoint(- withdrawRequest.getAmount());
		pointHistory.setBalancePoint(present);
		pointHistoryRepository.save(pointHistory);
		
		AES256Util aes256Util = new AES256Util(UsitCodeConstants.USIT_AES256_KEY);
		withdrawRequest.setResidentNumber(aes256Util.encrypt(withdrawRequest.getResidentNumber()));
		
		return withdrawRequestRepository.save(withdrawRequest);
	}
	
	
	

	public Page<WithdrawRequest> readAll(Pageable pageRequest,String periodCondition,String startDate,String endDate,String keywordCondition,String keyword) throws UnsupportedEncodingException, NoSuchAlgorithmException, GeneralSecurityException {

		
	     
		logger.info("pageable.getPageNumber():{}", pageRequest.getPageNumber());
	    
		logger.info("pageable.getPageSize():{}", pageRequest.getPageSize());
	        
	        
		// CriteriaBuilder 인스턴스를 작성한다.
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();


		// CriteriaQuery 인스턴스를 생성한다. Board 제네릭 형식으로...
		CriteriaQuery<WithdrawRequest> criteriaQuery = criteriaBuilder.createQuery(WithdrawRequest.class);
	    	
		// Root는 영속적 엔티티를 표시하는 쿼리 표현식이다. SQL의 FROM 과 유사함
		Root<WithdrawRequest> root = criteriaQuery.from(WithdrawRequest.class);

	    	
		List<Predicate> restrictions = new ArrayList<>();

	    	
		// SQL의 WHERE절이다. 조건부는 CriteriaBuilder에 의해 생성

	    	
		// 리스트로 작성
	    	
		Predicate expFromDate = null;
		Predicate expToDate = null;
		Predicate expKeyword = null;
		Predicate and = null;
	    	
	    
	    //기간 조건확인
	    	
		if (periodCondition.equals(UsitCodeConstants.PERIOD_CONDITION_REQUEST_DATE)) {
			expFromDate = criteriaBuilder.greaterThanOrEqualTo(root.get("regDate"),  TimeUtil.getStringToDateTime(startDate + "0000"));
			expToDate = criteriaBuilder.lessThanOrEqualTo(root.get("regDate"), TimeUtil.getStringToDateTime(endDate + "2359"));
		} else if (periodCondition.equals(UsitCodeConstants.PERIOD_CONDITION_PROCESS_DATE)) {
			expFromDate = criteriaBuilder.greaterThanOrEqualTo(root.get("modDate"), TimeUtil.getStringToDateTime(startDate + "0000"));
			expToDate = criteriaBuilder.lessThanOrEqualTo(root.get("modDate"), TimeUtil.getStringToDateTime(endDate + "2359"));
		}


		// 키워드 조건확인
		if (keywordCondition != null && !keywordCondition.equals("")) {


			if (keywordCondition.equals(UsitCodeConstants.KEYWORD_CONDITION_OWNER_NAME)) {

				expKeyword = criteriaBuilder.equal(root.get("ownerName"), keyword);
			} else if (keywordCondition.equals(UsitCodeConstants.KEYWORD_CONDITION_ACCOUNT_NUMBER)) {
				expKeyword = criteriaBuilder.equal(root.get("accountNumber"), keyword);

			} 
		
				and = criteriaBuilder.and(expFromDate, expToDate, expKeyword);
		} else {

				and = criteriaBuilder.and(expFromDate, expToDate);
		}

		restrictions.add(and);
		criteriaQuery.where(restrictions.toArray(new Predicate[] {}));

		// ORDER BY절. CriteriaQuery로 생성

		// 뭔가 복잡해 보여도 별거 없다. TypedQuery는 실행 결과를 리턴하는 타입이다.
		TypedQuery<WithdrawRequest> withdrawRequestListQuery = entityManager.createQuery(criteriaQuery)
				.setFirstResult(pageRequest.getPageNumber()).setMaxResults(pageRequest.getPageSize());
		List<WithdrawRequest> withdrawRequest = withdrawRequestListQuery.getResultList();
		
		int size = withdrawRequest.size();
//		List<WithdrawRequest> withdrawRequestResult = new ArrayList<WithdrawRequest>(size);
		
//		Collections.copy(withdrawRequestResult, withdrawRequest);
		
		
		ArrayList<WithdrawRequest> withdrawRequestResult = new ArrayList<WithdrawRequest>();
//		withdrawRequestResult.addAll(withdrawRequest);
		
//		ArrayList<WithdrawRequest> withdrawRequestResult = (ArrayList<WithdrawRequest>) ((List) ( (ArrayList) withdrawRequest).clone());
		//주민번호 복호화
		AES256Util aes256Util = new AES256Util(UsitCodeConstants.USIT_AES256_KEY);
		for (Iterator iterator = withdrawRequest.iterator(); iterator.hasNext();) {
			WithdrawRequest ori = (WithdrawRequest) iterator.next();
			WithdrawRequest clone = new WithdrawRequest();
			clone.setAccountNumber(ori.getAccountNumber());
			clone.setAddress(ori.getAddress());
			clone.setAddressDetail(ori.getAddressDetail());
			clone.setAmount(ori.getAmount());
			clone.setBankCd(ori.getBankCd());
			clone.setDenyReason(ori.getDenyReason());
			clone.setMember(ori.getMember());
			clone.setMemberId(ori.getMemberId());
			clone.setModDate(ori.getModDate());
			clone.setModId(ori.getModId());
			clone.setOwnerName(ori.getOwnerName());
			clone.setPostcode(ori.getPostcode());
			clone.setRegDate(ori.getRegDate());
			clone.setResidentNumber(aes256Util.decrypt(ori.getResidentNumber()));
			clone.setStatusCd(ori.getStatusCd());
			clone.setWithdrawRequestId(ori.getWithdrawRequestId());
			withdrawRequestResult.add(clone);
		}
		
		
		Page<WithdrawRequest> result = new PageImpl<WithdrawRequest>(withdrawRequestResult, pageRequest, size);

		logger.info("list.size():{}", result.getSize());
		return result;
		
		
		
	}
	
	
	
	
	
	public Page<WithdrawRequest> readAllByToken(Pageable pageRequest,Integer memberId) throws UnsupportedEncodingException, NoSuchAlgorithmException, GeneralSecurityException {
	
		
		List<WithdrawRequest> withdrawRequest = withdrawRequestRepository.findAllByMemberId(memberId);
		
		int size = withdrawRequest.size();
		List<WithdrawRequest> withdrawRequestResult = new ArrayList<WithdrawRequest>();
		
		//주민번호 복호화
		
		AES256Util aes256Util = new AES256Util(UsitCodeConstants.USIT_AES256_KEY);
		for (Iterator iterator = withdrawRequest.iterator(); iterator.hasNext();) {
			WithdrawRequest ori = (WithdrawRequest) iterator.next();
			WithdrawRequest clone = new WithdrawRequest();
			clone.setAccountNumber(ori.getAccountNumber());
			clone.setAddress(ori.getAddress());
			clone.setAddressDetail(ori.getAddressDetail());
			clone.setAmount(ori.getAmount());
			clone.setBankCd(ori.getBankCd());
			clone.setDenyReason(ori.getDenyReason());
			clone.setMember(ori.getMember());
			clone.setMemberId(ori.getMemberId());
			clone.setModDate(ori.getModDate());
			clone.setModId(ori.getModId());
			clone.setOwnerName(ori.getOwnerName());
			clone.setPostcode(ori.getPostcode());
			clone.setRegDate(ori.getRegDate());
			clone.setResidentNumber(aes256Util.decrypt(ori.getResidentNumber()));
			clone.setStatusCd(ori.getStatusCd());
			clone.setWithdrawRequestId(ori.getWithdrawRequestId());
			withdrawRequestResult.add(clone);
		}
		
		Page<WithdrawRequest> result = new PageImpl<WithdrawRequest>(withdrawRequestResult, pageRequest, size);
		
		return result;
	
	}
	
	
public WithdrawRequest modifyWithdrawRequest(WithdrawRequest withdrawRequest) {

	WithdrawRequest update = withdrawRequestRepository.findOne(withdrawRequest.getWithdrawRequestId());
	
	/**
	 * 출금요청이 반려 될 경우 포인트 원복 
	 */
	if(UsitCodeConstants.POINT_WITHDRAW_TYPE_CD_DENY.equals(withdrawRequest.getStatusCd())) {
		
		if(!UsitCodeConstants.POINT_WITHDRAW_TYPE_CD_STANDBY.equals(update.getStatusCd())) {
			logger.warn("승인요청건만 반려가 가능합니다.");
			throw new FrameworkException("-1001", "승인요청건만 반려가 가능합니다 시스템관리자에게 문의해주세요."); // 오류 리턴 예시
		}
		
		//포인트 증가
		Member member = memberService.getMemberByMemeberId(withdrawRequest.getMemberId());
		int present = member.getTotalPoint() + withdrawRequest.getAmount();
		member.setTotalPoint(present);
		member.setWithdrawablePoint(member.getWithdrawablePoint() + withdrawRequest.getAmount());
		memberRepository.save(member);
		
		//포인트 이력
		PointHistory pointHistory  = new PointHistory();
		pointHistory.setMemberId(withdrawRequest.getMemberId());
		pointHistory.setPointTypeCd(UsitCodeConstants.POINT_TYPE_CD_WITHDRAW_CANCEL);
		pointHistory.setAddPoint(withdrawRequest.getAmount());
		pointHistory.setBalancePoint(present);
		pointHistoryRepository.save(pointHistory);
		update.setStatusCd(withdrawRequest.getStatusCd());
		update.setDenyReason(withdrawRequest.getDenyReason());
		update.setModId(withdrawRequest.getModId());
	}else {	
		update.setStatusCd(withdrawRequest.getStatusCd());
		update.setModId(withdrawRequest.getModId());
	}
	
	
	return withdrawRequestRepository.save(update);
	}
	
	
	

	
}
