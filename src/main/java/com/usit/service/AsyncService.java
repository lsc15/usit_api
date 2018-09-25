package com.usit.service;

import java.util.ArrayList;

public interface AsyncService {

	void sendPromotionEmails(ArrayList<String> address,String title,String content) throws InterruptedException, Exception;
	
}