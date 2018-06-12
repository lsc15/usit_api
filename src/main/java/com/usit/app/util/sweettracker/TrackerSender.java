package com.usit.app.util.sweettracker;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


@Component
public class TrackerSender {
	private static Logger LOGGER = LoggerFactory.getLogger(TrackerSender.class);
	
	private String url;
	private String tKey;
	private String tCode;
	
	
	
    public String send2(String trackingNumber) throws Exception {

        HttpURLConnection con = null;
        DataOutputStream wr = null;
        BufferedReader in = null;
        int responseCode;
        String result="";
        try{

        	

//        	 url=url+"?t_key="+tKey+"&t_code="+tCode+"t_invoice=";
        	   String testUrl="http://info.sweettracker.co.kr/api/v1/companylist"+"?t_key="+tKey;
            URL obj = new URL(testUrl);
            con = (HttpURLConnection) obj.openConnection();

            con.setRequestMethod("GET");
            con.setRequestProperty("content-type", "application/json");


            responseCode = con.getResponseCode();
            LOGGER.debug("nSending 'GET' request to URL : {}", url);
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
                result=response.toString();
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

        return result;

    }
	
    
    
    
    
    
    
    
    
    
    
    
    public JSONObject send(String trackingNumber) throws ParseException, org.apache.http.ParseException, IOException {

        String getUrl = url+"?t_key="+tKey+"&t_code="+tCode+"&t_invoice="+trackingNumber;
        String testUrl="http://info.sweettracker.co.kr/api/v1/companylist"+"?t_key="+tKey;

        HttpClient client = HttpClientBuilder.create().build();
        HttpGet get = new HttpGet(getUrl);

        // add header
        get.setHeader("User-Agent", "Mozilla/5.0");


        HttpResponse response = client.execute(get);

        String json = EntityUtils.toString(response.getEntity());

        JSONParser parser = new JSONParser();

//        JSONObject res = (JSONObject) jo.get("response");
//        String resString = EntityUtils.toString(response.getEntity());

        return (JSONObject) parser.parse(json);

    }
    
    
    
    
    
    
    
    
    
    
    
    
    public  String getUrl() {
		return url;
	}
	public  void setUrl(String url) {
		this.url = url;
	}



	public String gettKey() {
		return tKey;
	}



	public void settKey(String tKey) {
		this.tKey = tKey;
	}


	public String gettCode() {
		return tCode;
	}


	public void settCode(String tCode) {
		this.tCode = tCode;
	}

	
    
    
    
    
}