package com.usit.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.usit.domain.UsitEmail;
import com.usit.domain.UsitEmailContent;

public interface AsyncService {

	void sendPromotionEmails(String from, String fromName, ArrayList<String> address, String title, String content);
	void sendBatchPromotionEmails(String successYn,Integer emailId);
	
	void savePromotionEmails(String from, String fromName, ArrayList<String> address, String title, String content);
	
	List<UsitEmail> getPromotionEmails(String sendDate);
	
	UsitEmailContent getPromotionEmailContent(int usitEmailContentId);
	
	Page<UsitEmail> getPromotionEmailsForAdmin(String sendDate,String sendStatus,Pageable pageable);
	
}