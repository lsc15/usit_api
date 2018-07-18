package com.usit.service;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.usit.domain.PointHistory;

public interface PointHistoryService {
	
	PointHistory addPoint(PointHistory point);
	
	Page<PointHistory> getPointListByMemberId(PageRequest page,int member);
}
