package com.usit.app.util.messenger.alimtalk;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class SweetTrackerSender {
	private static Logger LOGGER = LoggerFactory.getLogger(SweetTrackerSender.class);
	
	private String url;
	private String userId;
	private String userPwd;
	private String msgType;
    private String mtFailover;
    private String senderKey;
    private String senderId;
    private String responseMethod;
	
    
    
	
	
    public int send(Map<String, String> params) throws Exception {

        HttpURLConnection con = null;
        DataOutputStream wr = null;
        BufferedReader in = null;
        int responseCode;
        try{



            URL obj = new URL(url);
            con = (HttpURLConnection) obj.openConnection();

            con.setRequestMethod("POST");
            con.setRequestProperty("x-ib-client-id", userId);
            con.setRequestProperty("x-ib-client-passwd", userPwd);
            con.setRequestProperty("content-type", "application/json");

            Map<String, Object> kakaoParam = new HashMap<String, Object>();

            kakaoParam.put("msg_type", msgType);
            kakaoParam.put("mt_failover", mtFailover);

//            Map<String, Object> dataList = new HashMap<String, Object>();
            Map<String, Object> msgData = new HashMap<String, Object>();
            msgData.put("senderid",senderId);
            msgData.put("to", "82" + (params.get("to")).substring(1));
            msgData.put("content", params.get("content"));
//            dataList.add(msgData);
            kakaoParam.put("msg_data", msgData);
            
            
//            Map<String, Object> msgList = new HashMap<String, Object>();
            Map<String, Object> msgAttr = new HashMap<String, Object>();
            msgAttr.put("sender_key", senderKey);
            msgAttr.put("template_code", params.get("template_code"));
            msgAttr.put("response_method", responseMethod);
//            msgList.add(msgAttr);
            kakaoParam.put("msg_attr", msgAttr);

            String postJsonData = new ObjectMapper().writeValueAsString(kakaoParam);

            LOGGER.debug("postJsonData:{}", postJsonData);

            // Send post request
            con.setDoOutput(true);

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

        return responseCode;

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
	public String getMsgType() {
		return msgType;
	}
	public void setMsgType(String msgType) {
		this.msgType = msgType;
	}
	public String getMtFailover() {
		return mtFailover;
	}
	public void setMtFailover(String mtFailover) {
		this.mtFailover = mtFailover;
	}
	public String getSenderKey() {
		return senderKey;
	}
	public void setSenderKey(String senderKey) {
		this.senderKey = senderKey;
	}
	
	public String getSenderId() {
		return senderId;
	}

	public void setSenderId(String senderId) {
		this.senderId = senderId;
	}


	public String getResponseMethod() {
		return responseMethod;
	}
	public void setResponseMethod(String responseMethod) {
		this.responseMethod = responseMethod;
	}
    
    
    
    
    
}