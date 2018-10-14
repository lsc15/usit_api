package com.usit.app.util.messenger.alimtalk;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class NpaySender {
	private static Logger LOGGER = LoggerFactory.getLogger(NpaySender.class);
	
	private String url;
	private String userId;
	private String userPwd;
	
    
    
	
	
    public JSONObject send(Map<String, String> params) throws Exception {

        HttpURLConnection con = null;
        DataOutputStream wr = null;
        BufferedReader in = null;
        String json = "";
        int responseCode;
        JSONParser parser = new JSONParser();
        try{



            URL obj = new URL(url);
            con = (HttpURLConnection) obj.openConnection();

            con.setRequestMethod("POST");
            con.setRequestProperty("X-Naver-Client-Id", userId);
            con.setRequestProperty("X-Naver-Client-Secret", userPwd);
            con.setRequestProperty("content-type", "application/x-www-form-urlencoded");

            Map<String, Object> npayParam = new HashMap<String, Object>();

            npayParam.put("paymentId", params.get("paymentId"));
            String postJsonData = new ObjectMapper().writeValueAsString(npayParam);

            LOGGER.debug("postJsonData:{}", postJsonData);

            // Send post request
            con.setDoOutput(true);
            con.setConnectTimeout(60000);
            con.setReadTimeout(60000);

            wr = new DataOutputStream(con.getOutputStream());
            wr.write(postJsonData.getBytes("UTF-8"));
//            wr.write(postJsonData.getBytes());
            wr.flush();

            responseCode = con.getResponseCode();
            LOGGER.debug("nSending 'POST' request to URL : {}", url);
            LOGGER.debug("Response Code : {}", responseCode);

            if(responseCode == HttpURLConnection.HTTP_OK){

                in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String output;
                StringBuffer response = new StringBuffer();

                while ((output = in.readLine()) != null) {
                    response.append(output);
                }
                in.close();

                //printing result from response
                LOGGER.debug(response.toString());
                json = response.toString();
            } else {
                LOGGER.debug(con.getResponseMessage());

            }


        }catch(Exception e){
            throw e;
        }finally {
            if(in != null) {
                try {
                    in.close();

                }catch(Exception e) {

              }
          }
          if(wr != null) {
              try {
                  wr.close();
              }catch(Exception e) {

              }
          }
          if(con != null) {
              try {
                  con.disconnect();
              }catch(Exception e) {

              }
          }
      }

        return (JSONObject) parser.parse(json);

    }
	
    

    
    
    
    
    public  String getUrl() {
		return url;
	}
	public  void setUrl(String url) {
		this.url = url;
	}
    
    public String getUserId() {
		return userId;
	}
	
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getUserPwd() {
		return userPwd;
	}
	public void setUserPwd(String userPwd) {
		this.userPwd = userPwd;
	}
    
    
}