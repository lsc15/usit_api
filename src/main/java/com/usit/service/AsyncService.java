package com.usit.service;

import java.util.ArrayList;

public interface AsyncService {

	void sendPromotionEmails(String from, String fromName, ArrayList<String> address, String title, String content);
	
}