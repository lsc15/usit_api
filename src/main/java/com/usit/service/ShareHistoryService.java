package com.usit.service;

import com.usit.domain.ShareHistory;

public interface ShareHistoryService {

	ShareHistory getShareHistory(String storeKey);

	ShareHistory createShareHistory(int productId,String storeKey);
	
	ShareHistory createShareHistory(ShareHistory shareHistory);
	
	
	ShareHistory updateShareHistory(int productId,Integer shareId);
	
	ShareHistory updateShareHistory(ShareHistory shareHistory,Integer shareHistoryId);



}