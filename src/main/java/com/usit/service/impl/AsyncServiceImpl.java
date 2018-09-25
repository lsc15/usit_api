package com.usit.service.impl;


import java.util.ArrayList;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.usit.service.AsyncService;
import com.usit.util.MailUtil;

import lombok.RequiredArgsConstructor;

@Transactional
@RequiredArgsConstructor
@Service
public class AsyncServiceImpl implements AsyncService{
 
    private static Logger log = LoggerFactory.getLogger(AsyncServiceImpl.class);
 
 
    @Async
    public void sendPromotionEmails(ArrayList<String> address,String title,String content) throws Exception
    {
        log.info("sendPromotionEmails starts");
        log.info(String.join(",", address));
        MailUtil mu = new MailUtil();
        
        int times = 14;
        int limit = 14;
        int loop = address.size() / limit;
        int remainder = address.size() % limit;
        int index = 0;
        
        
        for (int i = 0; i < loop; i++) {
//    		ArrayList<String> temp = new ArrayList<String>();
        	for(int j = 0;j < times; j++) {
//        		temp.add(address.get(index));
        		mu.sendPromotionMail(address.get(index), title, content);
        		index++;
        		if(index == limit) {
//        			mu.sendPromotionMail(String.join(",", temp), title, content);
        			limit = limit + times;
        			Thread.sleep(1000L);    //Intentional delay
        			break;
        		}
        		
        	}
        	
    	}
//        ArrayList<String> temp = new ArrayList<String>();
        for (int l = 0; l < remainder; l++) {
//        	temp.add(address.get(index));
        	mu.sendPromotionMail(address.get(index), title, content);
        	index++;
        	
    	}
//        mu.sendPromotionMail(mail, title, content);
        
        
//        log.info("sendPromotionEmails, {}", sendPromotionEmails);
        
        log.info("sendPromotionEmails completed");
    }
 
 
}