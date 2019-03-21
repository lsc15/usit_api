package com.usit.service.impl;


import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.usit.app.spring.util.DateUtil;
import com.usit.domain.UsitEmail;
import com.usit.repository.UsitEmailRepository;
import com.usit.service.AsyncService;
import com.usit.util.MailUtil;
import com.usit.util.TimeUtil;

import lombok.RequiredArgsConstructor;

@Transactional
@RequiredArgsConstructor
@Service
public class AsyncServiceImpl implements AsyncService{
 
    private static Logger log = LoggerFactory.getLogger(AsyncServiceImpl.class);
 
    @Autowired
    private Environment env;
    
    @Autowired
	UsitEmailRepository usitEmailRepository;
    
    @Autowired
    JdbcTemplate jdbcTemplate;
    
 
    @Async
    public void sendPromotionEmails(String from, String fromName, ArrayList<String> address,String title,String content){
        log.info("sendPromotionEmails starts");
        log.info(String.join(",", address));
        MailUtil mu = new MailUtil();
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
    
    
    public void sendBatchPromotionEmails(List<UsitEmail> list){
        log.info("sendPromotionEmails starts");
        MailUtil mu = new MailUtil();
        int times = 14;
        int limit = 14;
        int loop = list.size() / limit;
        int remainder = list.size() % limit;
        int index = 0;
        
        
        for (int i = 0; i < loop; i++) {
        	for(int j = 0;j < times; j++) {
        		try {
        			Thread.sleep(100L);
					mu.sendPromotionMail(list.get(index).getFrom(), list.get(index).getFromName(), env.getProperty("aws.email.id"), env.getProperty("aws.email.pass"), list.get(index).getEmail(), list.get(index).getTitle(), list.get(index).getContent());
					jdbcTemplate.update("UPDATE usit_email SET send_status = ?, mod_date = ?  WHERE email_id = ?","Y",TimeUtil.getZonedDateTimeNow("Asia/Seoul").toString(),Long.valueOf(list.get(index).getEmailId()) );
				} catch (Exception e) {
					// TODO Auto-generated catch block
					jdbcTemplate.update("UPDATE usit_email SET send_status = ?, mod_date = ?  WHERE email_id = ?","F",TimeUtil.getZonedDateTimeNow("Asia/Seoul").toString(),list.get(index).getEmailId() );
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
        		Thread.sleep(100L);
				mu.sendPromotionMail(list.get(index).getFrom(), list.get(index).getFromName(), env.getProperty("aws.email.id"), env.getProperty("aws.email.pass"), list.get(index).getEmail(), list.get(index).getTitle(), list.get(index).getContent());
				jdbcTemplate.update("UPDATE usit_email SET send_status = ?, mod_date = ?  WHERE email_id = ?","Y",TimeUtil.getZonedDateTimeNow("Asia/Seoul").toString(),list.get(index).getEmailId() );
			} catch (Exception e) {
				// TODO Auto-generated catch block
				jdbcTemplate.update("UPDATE usit_email SET send_status = ?, mod_date = ?  WHERE email_id = ?","F",TimeUtil.getZonedDateTimeNow("Asia/Seoul").toString(),list.get(index).getEmailId() );
				e.printStackTrace();
			}
        	index++;
        	
    	}
        
        
        
        log.info("sendPromotionEmails completed");
    }
    
    
    
    
    public void savePromotionEmails(String from, String fromName, ArrayList<String> address,String title,String content){
        log.info("savePromotionEmails starts");
        
        List<UsitEmail> list = new ArrayList<UsitEmail>();
        for (String mail : address) {
        	UsitEmail email = new UsitEmail();
        	email.setEmail(mail);
        	email.setFrom(from);
        	
//            if("usitstorelink@usit.co.kr".equals(from)) {
//            	fromName = "storelink";
//            }else {
//            	fromName = "usit";
//            }
            email.setSendDate(DateUtil.getCurrDate());
        	email.setFromName(fromName);
        	email.setTitle(title);
        	email.setSendStatus("N");
        	email.setContent(content);
        	email.setRegId(0);
        	email.setRegDate(TimeUtil.getZonedDateTimeNow("Asia/Seoul"));
        	list.add(email);
			
		}
        usitEmailRepository.save(list);
        
        log.info("savePromotionEmails completed");
    }
    
    
    public List<UsitEmail> getPromotionEmails(String sendDate){
        log.info("getPromotionEmails starts");
        	
        	
        List<UsitEmail> list = usitEmailRepository.findTop1000BySendDateGreaterThanAndSendStatus(sendDate,"N");
			
        
        log.info("getPromotionEmails completed");
		return list;
    }
 
 
    
    
    public Page<UsitEmail> getPromotionEmailsForAdmin(String sendDate,String sendStatus,Pageable pageable){
    	Page<UsitEmail> list;
        if(sendStatus.equals("")||sendStatus == null) {
        	list = usitEmailRepository.findBySendDate(sendDate,pageable);
        }else {
        	list = usitEmailRepository.findBySendDateAndSendStatus(sendDate,sendStatus,pageable);
        }
        
			
        
		return list;
    }
    
    
    
    
    
    
}