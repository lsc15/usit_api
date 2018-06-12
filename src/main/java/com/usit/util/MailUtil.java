package com.usit.util;


import java.text.SimpleDateFormat;
import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.util.Random;

public class MailUtil {

	private boolean lowerCheck;
    private int size;
    private static Logger LOGGER = LoggerFactory.getLogger(MailUtil.class);
    // Replace sender@example.com with your "From" address.
    // This address must be verified.
    static final String FROM = "";
    static final String FROMNAME = "info";
	
    // Replace recipient@example.com with a "To" address. If your account 
    // is still in the sandbox, this address must be verified.
    String to = "";
    
    // Replace smtp_username with your Amazon SES SMTP user name.
    static final String SMTP_USERNAME = "AKIAIF4X6HOQL2RVZMBA";
    
    // Replace smtp_password with your Amazon SES SMTP password.
    static final String SMTP_PASSWORD = "Aplxu1DxgvaNRm6UhuugcY4GWX+LHDa/lETw2vX1LUUh";
    
    // The name of the Configuration Set to use for this message.
    // If you comment out or remove this variable, you will also need to
    // comment out or remove the header below.
    static final String CONFIGSET = "ConfigSet";
    
    // Amazon SES SMTP host name. This example uses the US West (Oregon) Region.
    static final String HOST = "email-smtp.us-west-2.amazonaws.com";
    
    // The port you will connect to on the Amazon SES SMTP endpoint. 
    static final int PORT = 587;
    
    static final String SUBJECT = "본인 인증메일";
    static final String SUBJECT_PASS_MAIL = "임시 비밀번호";
    
    String body = "";

    public void mail(String mail,String token) throws Exception {
    	
    	
   
    	Date date = new Date();
   
    	SimpleDateFormat sdformat = new SimpleDateFormat("yyyyMd");
   
    	int today = Integer.parseInt(sdformat.format(date));

   
    	to = mail;

    //오픈일 이후부터 ssl적용
    	if(today > 2018322) {
    		body =
    		    	String.join(
    		        	    System.getProperty("line.separator"),
    		        	    "<h1>서비스 가입을 감사드립니다!</h1>",
    		        	    "<p>고객님의 건강을 기원하며 저희와 함께 하기 위한 인증절차를 진행부탁드립니다. ", 
    		        	    "<a href='http://www/email-send-complete?email="+mail+"&token="+token+"'><B><U>인증하기</U></B></a> 유효기간:1일",
    		        	    "<p>감사합니다."
    		        	);
    	}else {
    		body =
    		    	String.join(
    		        	    System.getProperty("line.separator"),
    		        	    "<h1> 서비스 가입을 감사드립니다!</h1>",
    		        	    "<p>고객님의 건강을 기원하며 저희와 함께 하기 위한 인증절차를 진행부탁드립니다. ", 
    		        	    "<a href='http://www/email-send-complete?email="+mail+"&token="+token+"'><B><U>인증하기</U></B></a> 유효기간:1일",
    		        	    "<p>감사합니다."
    		        	);
    	}

    	
    	
    	
    // Create a Properties object to contain connection configuration information.
    	Properties props = System.getProperties();
    	props.put("mail.transport.protocol", "smtp");
    	props.put("mail.smtp.port", PORT); 
    	props.put("mail.smtp.starttls.enable", "true");
    	props.put("mail.smtp.auth", "true");

    // Create a Session object to represent a mail session with the specified properties. 
    	Session session = Session.getDefaultInstance(props);

    // Create a message with the specified information. 
    MimeMessage msg = new MimeMessage(session);
    msg.setFrom(new InternetAddress(FROM,FROMNAME));
    msg.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
    msg.setSubject(SUBJECT);
    msg.setContent(body,"text/html; charset=euc-kr");
 // Add a configuration set header. Comment or delete the 
 // next line if you are not using a configuration set
//        msg.setHeader("X-SES-CONFIGURATION-SET", CONFIGSET);
            
 // Create a transport.
    Transport transport = session.getTransport();
                    
        // Send the message.
        try
        {
            LOGGER.info("Sending...");
            
            // Connect to Amazon SES using the SMTP username and password you specified above.
            transport.connect(HOST, SMTP_USERNAME, SMTP_PASSWORD);
        	
            // Send the email.
            transport.sendMessage(msg, msg.getAllRecipients());
            LOGGER.info("Email sent!");
        }
        catch (Exception ex) {
            LOGGER.error("The email was not sent.");
            LOGGER.error("Error message: " + ex.getMessage());
        }
        finally
        {
            // Close and terminate the connection.
            transport.close();
        }
    }
    
    
    
    
    //비밀번호찾기 메일
    public void sendPassMail(String mail,String pass) throws Exception {
    	
    	to = mail;

    		body =
    		    	String.join(
    		        	    System.getProperty("line.separator"),
    		        	    "<h1>임시 비밀번호 발급</h1>",
    		        	    "<p>고객님의 비밀번호가 아래와 같이 변경되었습니다. 접속 후 비밀번호 변경을 부탁드립니다. ", 
    		        	    "<p>임시비밀번호 : "+pass
    		        	);

    	
    	
    	
    // Create a Properties object to contain connection configuration information.
    	Properties props = System.getProperties();
    	props.put("mail.transport.protocol", "smtp");
    	props.put("mail.smtp.port", PORT); 
    	props.put("mail.smtp.starttls.enable", "true");
    	props.put("mail.smtp.auth", "true");

    // Create a Session object to represent a mail session with the specified properties. 
    	Session session = Session.getDefaultInstance(props);

    // Create a message with the specified information. 
    MimeMessage msg = new MimeMessage(session);
    msg.setFrom(new InternetAddress(FROM,FROMNAME));
    msg.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
    msg.setSubject(SUBJECT_PASS_MAIL);
    msg.setContent(body,"text/html; charset=euc-kr");
 // Add a configuration set header. Comment or delete the 
 // next line if you are not using a configuration set
//        msg.setHeader("X-SES-CONFIGURATION-SET", CONFIGSET);
            
 // Create a transport.
    Transport transport = session.getTransport();
                    
        // Send the message.
        try
        {
            LOGGER.info("Sending...");
            
            // Connect to Amazon SES using the SMTP username and password you specified above.
            transport.connect(HOST, SMTP_USERNAME, SMTP_PASSWORD);
        	
            // Send the email.
            transport.sendMessage(msg, msg.getAllRecipients());
            LOGGER.info("Email sent!");
        }
        catch (Exception ex) {
            LOGGER.error("The email was not sent.");
            LOGGER.error("Error message: " + ex.getMessage());
        }
        finally
        {
            // Close and terminate the connection.
            transport.close();
        }
    }
    
    
          
    		//이메일 인증에 사용할 토큰값 생성
        public String getKey(int size, boolean lowerCheck) {
            this.size = size;
            this.lowerCheck = lowerCheck;
            return init();
        }

        private String init() {
            Random ran = new Random();
            StringBuffer sb = new StringBuffer();
            int num = 0;
            do {
                num = ran.nextInt(75)+48;
                if((num>=48 && num<=57) || (num>=65 && num<=90) || (num>=97 && num<=122)) {
                    sb.append((char)num);
                }else {
                    continue;
                }
            } while (sb.length() < size);
            if(lowerCheck) {
                return sb.toString().toUpperCase();
            }
            return sb.toString();
        }

    
}
