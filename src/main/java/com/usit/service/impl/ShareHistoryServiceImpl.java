package com.usit.service.impl;


import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.usit.app.spring.exception.FrameworkException;
import com.usit.app.spring.util.AES256Util;
import com.usit.app.spring.util.UsitCodeConstants;
import com.usit.domain.PointHistory;
import com.usit.domain.ShareHistory;
import com.usit.domain.UsitOrder;
import com.usit.repository.MemberRepository;
import com.usit.repository.ShareHistoryRepository;
import com.usit.service.ShareHistoryService;
import com.usit.util.TimeUtil;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class ShareHistoryServiceImpl implements ShareHistoryService{

	private static Logger LOGGER = LoggerFactory.getLogger(ShareHistoryServiceImpl.class);



	@Autowired
	ShareHistoryRepository shareHistoryRepository;

	@Autowired
	MemberRepository memberRepository;

	@PersistenceContext
	EntityManager entityManager;

	
	public ShareHistory getShareHistory(int productId,String storeKey) {
		
		AES256Util aes256Util = null;
		String uId = null;
		String today = null;
		Integer memberId = null;
		try {
			aes256Util = new AES256Util(UsitCodeConstants.USIT_AES256_KEY);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		try {
			uId = aes256Util.decrypt(storeKey);
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
			today = TimeUtil.getZonedDateTimeNow("Asia/Seoul").format(formatter);
			
			memberId = memberRepository.findByMemberUid(uId).getMemberId();
		} catch (UnsupportedEncodingException | GeneralSecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			LOGGER.warn("공유 히스토리 저장 실패.");
			throw new FrameworkException("-1001", "공유 URL이 올바르지 않습니다."); // 오류 리턴 예시
		}
     	
		
		return shareHistoryRepository.findByDateAndMemberIdAndProductId(today, memberId, productId);

	}
	
	
	

	public List<ShareHistory> getShareHistoryByMemberId(Integer memberId) {
		return shareHistoryRepository.findByMemberId(memberId);
	}
	
	
	
	// 추천인 이벤트
	@Override
    public List<PointHistory> getEventMemberForAddPoint(String today) {



    	
    	
    	TypedQuery<PointHistory> countQuery = entityManager.createQuery(
				"SELECT distinct p FROM PointHistory p " +
				"WHERE p.pointTypeCd = 1511 AND p.memberId in " +
				"(SELECT DISTINCT memberId " + 
				"FROM ShareHistory " + 
				"WHERE date = :date AND purchaseCnt > 0 ) group by memberId having count(memberId) < 2 ", PointHistory.class
        		 ).setParameter("date", today);

        List<PointHistory> list = countQuery.getResultList();

        LOGGER.info("resultList.size():{}", list.size());
        
        

         return list;
    }
	

	

	//view증가용 공유히스토리
	public ShareHistory createShareHistory(int productId,String storeKey) {
		
		AES256Util aes256Util = null;
		String uId = null;
		try {
			aes256Util = new AES256Util(UsitCodeConstants.USIT_AES256_KEY);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			uId = aes256Util.decrypt(storeKey);
		} catch (UnsupportedEncodingException | GeneralSecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
     	
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
		String today = TimeUtil.getZonedDateTimeNow("Asia/Seoul").format(formatter);
		
		Integer memberId = memberRepository.findByMemberUid(uId).getMemberId();
		
	    ShareHistory share = new ShareHistory();
	    share.setMemberId(memberId);
	    share.setStoreKey(storeKey);
	    share.setProductId(productId);
		share.setDate(today);
		share.setVisitCnt(1);
		return shareHistoryRepository.save(share);
	}
	
	
	//purchase증감용 공유히스토리
	public ShareHistory createShareHistory(ShareHistory shareHistory) {
		
		return shareHistoryRepository.save(shareHistory);
	}
	
	
	
	

	//view증가 업데이트 공유히스토리
	public ShareHistory updateShareHistory(int productId,Integer shareId) {
		ShareHistory updateShareHistory = shareHistoryRepository.findOne(shareId);
		
		if(updateShareHistory==null) {
			LOGGER.warn("해당 히스토리가 없습니다.");
			throw new FrameworkException("-1001", "존재하지 않는 히스토리입니다"); // 오류 리턴 예시
		}else{
			updateShareHistory.setVisitCnt(updateShareHistory.getVisitCnt() + 1);
		return shareHistoryRepository.save(updateShareHistory);
			
		}
		
		
		
	}
	
	
	/**
	 * 보안상 주문 callback시 서버처리
	 * 
	 */
	//purchase증감용 공유히스토리
//	public ShareHistory updateShareHistory(ShareHistory shareHistory,Integer shareHistoryId) {
//		ShareHistory updateShareHistory = shareHistoryRepository.findOne(shareHistoryId);
//		
//		if(updateShareHistory==null) {
//			LOGGER.warn("해당 히스토리가 없습니다.");
//			throw new FrameworkException("-1001", "존재하지 않는 히스토리입니다"); // 오류 리턴 예시
//		}else{
//			updateShareHistory.setPurchaseCnt(updateShareHistory.getPurchaseCnt() + shareHistory.getPurchaseCnt());
//			updateShareHistory.setPurchaseAmount(updateShareHistory.getPurchaseAmount() + shareHistory.getPurchaseAmount());
//		return shareHistoryRepository.save(updateShareHistory);
//			
//		}
//		
//		
//		
//	}

	

	
}
