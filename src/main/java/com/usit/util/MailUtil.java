package com.usit.util;


import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jxl.Sheet;
import jxl.Workbook;

import java.util.Random;

public class MailUtil {

	private boolean lowerCheck;
    private int size;
    private static Logger LOGGER = LoggerFactory.getLogger(MailUtil.class);
    // Replace sender@example.com with your "From" address.
    // This address must be verified.
    static final String FROM = "admin@usit.co.kr";
    static final String FROMNAME = "admin";
	
    // Replace recipient@example.com with a "To" address. If your account 
    // is still in the sandbox, this address must be verified.
    String to = "";
    
    // Replace smtp_username with your Amazon SES SMTP user name.
    static final String SMTP_USERNAME = "AKIAJANSHG37D2I4B67Q";
    
    // Replace smtp_password with your Amazon SES SMTP password.
    static final String SMTP_PASSWORD = "Ak6gZzTkhRdU6iewWJFJ0FdkOidf1VGWO2tch70v01NP";
    
    // The name of the Configuration Set to use for this message.
    // If you comment out or remove this variable, you will also need to
    // comment out or remove the header below.
    static final String CONFIGSET = "ConfigSet";
    
    // Amazon SES SMTP host name. This example uses the US West (Oregon) Region.
    static final String HOST = "email-smtp.us-west-2.amazonaws.com";
    
    // The port you will connect to on the Amazon SES SMTP endpoint. 
    static final int PORT = 587;
    
    static final String SUBJECT = "[유짓]비회원 구매정보";
    static final String SUBJECT_PASS_MAIL = "임시 비밀번호";
    
    String body = "";

    public void mail(String SMTP_USERNAME,String SMTP_PASSWORD,String mail,String token) throws Exception {
    	
    	
   
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
    public void sendPassMail(String SMTP_USERNAME, String SMTP_PASSWORD,String mail,String pass) throws Exception {
    	
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
    
    
    
    
    
    public void anonymousSendMail(String SMTP_USERNAME, String SMTP_PASSWORD, String mail,int orderId,String ordererPhone) throws Exception {
    	
    	
    	to = mail;

    		body =
    		    	String.join(
    		        	    System.getProperty("line.separator"),
    		        	    "<h1> 상품주문에 감사드립니다!</h1>",
    		        	    "<p>주문하신 상품의 정보를 통해 유짓에서 배송추적이 가능합니다. ",
    		        	    "<p>주문번호 : "+orderId,
    		        	    "<p>주문자 휴대폰 번호 : "+ordererPhone,
    		        	    "<p><a href='https://www.usit.co.kr'><B><U>유짓 사이트가기</U></B></a>",
    		        	    "<p>감사합니다."
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
    
    
    
    
    
  //홍보 메일
    public void sendPromotionMail(String from, String fromName, String SMTP_USERNAME,String SMTP_PASSWORD, String mail,String title, String content) throws Exception {
    	to = mail;

    		body =
    		    	String.join(
    		        	    System.getProperty("line.separator"),
    		        	    content
    		        	);

    	
    	
    	
    // Create a Properties object to contain connection configuration information.
    	Properties props = System.getProperties();
    	props.put("mail.transport.protocol", "smtp");
    	props.put("mail.smtp.port", PORT); 
    	props.put("mail.smtp.starttls.enable", "true");
    	props.put("mail.smtp.auth", "true");

    // Create a Session object to represent a mail session with the specified properties. 
    	Session session = Session.getDefaultInstance(props);

//    String unsubscribe ="<br><br><p>수신거부 <a href=\"http://www.usit.co.kr/unsubscribe/"+to+"\" target=\"_blank\" \">Unsubscribe</a></p>";
    String unsubscribe ="<br><br><p>수신거부 <a href=\"http://usitapidev-env.9h8urwgtr9.ap-northeast-2.elasticbeanstalk.com/codes/unsubscribe?email="+to+"\" target=\"_blank\" \">Unsubscribe</a></p>";
    	
    // Create a message with the specified information. 
    MimeMessage msg = new MimeMessage(session);
    msg.setFrom(new InternetAddress(from,fromName));
    msg.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
    msg.setSubject(title);
    msg.setContent(body+unsubscribe,"text/html; charset=euc-kr");
 // Add a configuration set header. Comment or delete the 
 // next line if you are not using a configuration set
//        msg.setHeader("X-SES-CONFIGURATION-SET", CONFIGSET);
            
 // Create a transport.
    Transport transport = session.getTransport();
                    
        // Send the message.
        try
        {
            LOGGER.info(to +" Sending...");
            
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

        
        
        //엑셀파일 읽기
        public  ArrayList<String> readExcel(File mailAddress){
    		
    		
   		 Workbook workbook = null;
   		    Sheet sheet = null;
//   		    Cell cell = null;
   		    ArrayList<String> address = new ArrayList<String>();

   		    try
   		    {
   		        // 엑셀파일을 인식
   		        workbook = Workbook.getWorkbook( new File(mailAddress.getAbsolutePath()));

   		        // 엑셀파일에 포함된 sheet의 배열을 리턴한다.
   		        //workbook.getSheets();

   		        if( workbook != null)
   		        {
   		            // 엑셀파일에서 첫번째 Sheet를 인식
   		            sheet = workbook.getSheet(0);

   		            if( sheet != null)
   		            {
   		                // 셀인식 Cell a1 = sheet.getCell( 컬럼 Index, 열 Index);
   		                // 셀 내용 String stringa1 = a1.getContents();

   		                // 기록물철의 경우 실제 데이터가 시작되는 Row지정
   		                int nRowStartIndex = 1;
   		                // 기록물철의 경우 실제 데이터가 끝 Row지정
   		                int nRowEndIndex   = sheet.getColumn(0).length - 1;

   		                // 기록물철의 경우 실제 데이터가 시작되는 Column지정
//   		                int nColumnStartIndex = 0;
   		                // 기록물철의 경우 실제 데이터가 끝나는 Column지정
//   		                int nColumnEndIndex = sheet.getRow(1).length - 1;

   		                String szValue = "";

   		                for( int nRow = nRowStartIndex; nRow <= nRowEndIndex; nRow++ )
   		                {
//   		                    for( int nColumn = nColumnStartIndex; nColumn <= nColumnEndIndex ; nColumn++)
//   		                    {
   		                        szValue = sheet.getCell( 0, nRow).getContents();
   		                        address.add(szValue);
//   		                        System.out.print( szValue);
//   		                        System.out.print( "\t" );
//   		                    }
//   		                    System.out.println();
   		                }
   		            }
   		            else
   		            {
   		            	LOGGER.info( "Sheet is null!!" );
   		            }
   		        }
   		        else
   		        {
   		        	LOGGER.info( "WorkBook is null!!" );
   		        }
   		    }
   		    catch( Exception e)
   		    {
   		        e.printStackTrace();
   		    }
   		    finally
   		    {
   		        if( workbook != null)
   		        {
   		            workbook.close();
   		        }
   		    }
			return address;
   		
   	}
    
}
