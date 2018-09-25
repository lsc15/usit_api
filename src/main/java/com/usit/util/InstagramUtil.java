package com.usit.util;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.SecureRandom;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InstagramUtil {

    /**
     * @param startNum
     * @param endNum
     * @return
     */
	private static Logger logger = LoggerFactory.getLogger(InstagramUtil.class);
    public boolean getInstagramFeed(String resource) {

		 
        HttpURLConnection connection = null;
 
        boolean result = false;
        try {
            // 요청 URL
            URL url = new URL(resource);
            // 문자열로 URL 표현
            System.out.println("URL :" + url.toExternalForm());
            connection = (HttpURLConnection) url.openConnection();
            // 요청 방식(GET or POST)
            connection.setRequestMethod("GET");
            // 요청응답 타임아웃 설정
            connection.setConnectTimeout(3000);
            // 읽기 타임아웃 설정
            connection.setReadTimeout(3000);
 
            // 요청한 URL에 대한 응답 내용 출력.
            BufferedReader reader = 
                    new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
            StringBuffer buffer = new StringBuffer();
            String line = null;
            while ((line = reader.readLine()) != null) {
                buffer.append(line).append("\r\n");
            }
            reader.close();
 
//            System.out.println(buCffer.toString());
            
            
            Document doc = Jsoup.parse(buffer.toString());
            Elements rows = doc.select("[name='description']");
            if(rows.size() > 0 ) {
            result = true;
            logger.info("validation feed: " +doc.getElementsByAttributeValue("name", "description"));
            }else {
            	result = false;
            }            
            
            
 
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
 
        }
		return result;
    }

}
