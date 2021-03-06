package com.usit.service;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.usit.domain.PointHistory;

public interface PointHistoryService {
	
	PointHistory addPoint(PointHistory point);
	
	//이벤트
	void addPointEvent(PointHistory point);
	
	Page<PointHistory> getPointListByMemberId(PageRequest page,int member);
	
	Page<PointHistory> getPointSummaryByMemberId(PageRequest page,int member);
	
}
