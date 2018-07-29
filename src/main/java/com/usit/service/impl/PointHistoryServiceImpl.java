package com.usit.service.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.usit.app.spring.service.CommonHeaderService;
import com.usit.domain.Member;
import com.usit.domain.PointHistory;
import com.usit.repository.MemberRepository;
import com.usit.repository.PointHistoryRepository;
import com.usit.service.PointHistoryService;

@Service
@Transactional
public class PointHistoryServiceImpl extends CommonHeaderService implements PointHistoryService {



	@Autowired
	private PointHistoryRepository pointHistoryRepository;
	
	@Autowired
	private MemberRepository memberRepository;

	
	public PointHistory addPoint(PointHistory point) {
		
		PointHistory returnPoint =  pointHistoryRepository.save(point);
		Member member = memberRepository.findOne(returnPoint.getMemberId());
		member.setTotalPoint(member.getTotalPoint()+returnPoint.getAddPoint());
	    memberRepository.save(member);
		
		return returnPoint;
	}
	
	public Page<PointHistory> getPointListByMemberId(PageRequest page,int memberId){
		
		
		Page<PointHistory> list =  pointHistoryRepository.findAllByMemberId(page,memberId);
		
		return list;
		
	}
	
	
	public Page<PointHistory> getPointSummaryByMemberId(PageRequest page,int memberId){
		
		
		Page<PointHistory> list =  pointHistoryRepository.findAllByMemberId(page,memberId);
		
		return list;
		
	}
	
	
	
	
}
