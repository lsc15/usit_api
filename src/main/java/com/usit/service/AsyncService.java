package com.usit.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.usit.domain.UsitEmail;

public interface AsyncService {

	void sendPromotionEmails(String from, String fromName, ArrayList<String> address, String title, String content);
	void sendBatchPromotionEmails(List<UsitEmail> list);
	
	void savePromotionEmails(String from, String fromName, ArrayList<String> address, String title, String content);
	
	List<UsitEmail> getPromotionEmails(String sendDate);
	
	Page<UsitEmail> getPromotionEmailsForAdmin(String sendDate,String sendStatus,Pageable pageable);
	
}