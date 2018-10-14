package com.usit.service.impl;


import java.util.ArrayList;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
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
 
    @Autowired
    private Environment env;
    
 
    @Async
    public void sendPromotionEmails(String from, String fromName, ArrayList<String> address,String title,String content){
        log.info("sendPromotionEmails starts");
        log.info(String.join(",", address));
        MailUtil mu = new MailUtil();
//        mu.sendPromotionMail(from, env.getProperty("aws.email.id"), env.getProperty("aws.email.pass"), address, title, content);
        int times = 14;
        int limit = 14;
        int loop = address.size() / limit;
        int remainder = address.size() % limit;
        int index = 0;
        
        
        for (int i = 0; i < loop; i++) {
        	for(int j = 0;j < times; j++) {
        		try {
					mu.sendPromotionMail(from, fromName, env.getProperty("aws.email.id"), env.getProperty("aws.email.pass"), address.get(index), title, content);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        		index++;
        		if(index == limit) {
        			limit = limit + times;
        			try {
						Thread.sleep(1100L);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}    //Intentional delay
        			break;
        		}
        		
        	}
        	
    	}
        for (int l = 0; l < remainder; l++) {
        	try {
				mu.sendPromotionMail(from, fromName,  env.getProperty("aws.email.id"), env.getProperty("aws.email.pass"), address.get(index), title, content);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        	index++;
        	
    	}
        
        
        
        log.info("sendPromotionEmails completed");
    }
 
 
}