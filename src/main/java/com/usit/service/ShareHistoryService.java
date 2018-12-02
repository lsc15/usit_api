package com.usit.service;

import java.util.List;
import java.util.Map;

import com.usit.domain.PointHistory;
import com.usit.domain.ShareHistory;

public interface ShareHistoryService {

	ShareHistory getShareHistory(int productId,String storeKey);
	
	List<ShareHistory> getShareHistoryByMemberId(Integer memberId);

	ShareHistory createShareHistory(int productId,String storeKey);
	
	ShareHistory createShareHistory(ShareHistory shareHistory);
	
	
	ShareHistory updateShareHistory(int productId,Integer shareId);
	
	
	// 추천인 이벤트
	List<PointHistory> getEventMemberForAddPoint(String today);
//	ShareHistory updateShareHistory(ShareHistory shareHistory,Integer shareHistoryId);



}