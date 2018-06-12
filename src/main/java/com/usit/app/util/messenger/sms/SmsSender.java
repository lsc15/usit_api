package com.usit.app.util.messenger.sms;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.usit.app.util.messenger.MessageSender;

@Component
public class SmsSender implements MessageSender{

    private static Logger LOGGER = LoggerFactory.getLogger(SmsSender.class);

    private String apiUrlForToken;
    private String apiUrlForRequest;
    private String userId;
    private String userPwd;
    private String authKey;

    public String getApiUrlForToken() {
        return apiUrlForToken;
    }

    public void setApiUrlForToken(String apiUrlForToken) {
        this.apiUrlForToken = apiUrlForToken;
    }
    public String getApiUrlForRequest() {
        return apiUrlForRequest;
    }

    public void setApiUrlForRequest(String apiUrlForRequest) {
        this.apiUrlForRequest = apiUrlForRequest;
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

    public String getAuthKey() {
        return authKey;
    }

    public void setAuthKey(String authKey) {
        this.authKey = authKey;
    }

    @Override
    public void send(Map<String, Object> params) throws Exception {

        HttpURLConnection con = null;
        DataOutputStream wr = null;
        BufferedReader in = null;

        try{

            String res = genAuthToken();

            Map<String, String> resMap = new ObjectMapper().readValue(res, new TypeReference<HashMap<String, String>>() {});

            LOGGER.debug("authToken:{}", resMap.toString());
            authKey = (String)resMap.get("accessToken");

            URL obj = new URL(apiUrlForRequest);
            con = (HttpURLConnection) obj.openConnection();

            con.setRequestMethod("POST");
            con.setRequestProperty("Authorization", "Basic " + authKey);
            con.setRequestProperty("Accept", "application/json");
            con.setRequestProperty("Content-Type","application/json");

            Map<String, Object> paramMsg = new HashMap<String, Object>();

            paramMsg.put("title", params.get("title"));
            paramMsg.put("text", params.get("text"));
            paramMsg.put("from", "18550023");
            paramMsg.put("ttl", 100);
            paramMsg.put("fileKey", "");
            paramMsg.put("ref", "");
            paramMsg.put("paymentCode", "");
            paramMsg.put("clientSubId", "");

            List<Map<String, Object>> listDest = new ArrayList<Map<String, Object>>();
            Map<String, Object> destinations = new HashMap<String, Object>();
            destinations.put("to", (String)params.get("to"));
            destinations.put("to", "+82" + ((String)params.get("to")).substring(1));
            destinations.put("replaceWord1", "");
            destinations.put("replaceWord2", "");
            destinations.put("replaceWord3", "");
            destinations.put("replaceWord4", "");
            destinations.put("replaceWord5", "");
            listDest.add(destinations);
            paramMsg.put("destinations", listDest);

//            String postJsonData = "{"id":5,"countryName":"USA","population":8000}";
            String postJsonData = new ObjectMapper().writeValueAsString(paramMsg);

            LOGGER.debug("postJsonData:{}", postJsonData);

            // Send post request
            con.setDoOutput(true);
//            outputStreamWriter = new OutputStreamWriter(con.getOutputStream());
//            outputStreamWriter.write(postJsonData);

            wr = new DataOutputStream(con.getOutputStream());
            wr.write(postJsonData.getBytes("UTF-8"));
//            wr.writeBytes(postJsonData);
            wr.flush();
//            wr.close();

            int responseCode = con.getResponseCode();
            LOGGER.debug("nSending 'POST' request to URL : {}", apiUrlForRequest);
//            LOGGER.debug("Post Data : " + postJsonData);
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


    }

    public String genAuthToken() throws Exception {

        HttpURLConnection con = null;
        DataOutputStream wr = null;
        BufferedReader in = null;

        String authToken = "";

        try{

              URL obj = new URL(apiUrlForToken);
              con = (HttpURLConnection) obj.openConnection();

              con.setRequestMethod("POST");
              con.setRequestProperty("X-IB-Client-Id", userId);
              con.setRequestProperty("X-IB-Client-Passwd", userPwd);
              con.setRequestProperty("Accept", "application/json");

              LOGGER.debug("{}", con.toString());

              String postJsonData = "{}";

              con.setDoOutput(true);
              con.setDoInput(true);
              wr = new DataOutputStream(con.getOutputStream());
              wr.writeBytes(postJsonData);
              wr.flush();
              wr.close();

              int responseCode = con.getResponseCode();
              LOGGER.debug("nSending 'POST' request to URL : {}", apiUrlForToken);
              LOGGER.debug("Response Code :{}", responseCode);

              in = new BufferedReader(
                      new InputStreamReader(con.getInputStream()));
              String output;
              StringBuffer response = new StringBuffer();

              while ((output = in.readLine()) != null) {
               response.append(output);
              }
              in.close();

              //printing result from response
              LOGGER.debug(response.toString());
              authToken = response.toString();

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

        return authToken;

    }

}
