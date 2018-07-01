package com.usit.app.util.sweettracker;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.usit.util.TimeUtil;


@Component
public class TrackerSender {
	private static Logger LOGGER = LoggerFactory.getLogger(TrackerSender.class);
	
	private String url;
	private String tKey;
	private String tCode;
	private String tTier;
	private String cUscde;
	
	
	
	
	
	
	
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
	
    
    
    
    
    
    
    
    
  //배송조회
    public JSONObject sendCompanylistApi() throws ParseException, org.apache.http.ParseException, IOException {

        String getUrl = url+"?t_key="+tKey;
//        String testUrl="http://info.sweettracker.co.kr/api/v1/companylist"+"?t_key="+tKey;

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
    
    
    
    //배송조회
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
    
    
    
    

	//반송가능일 확인
	public JSONObject checkSweetTrackerReturnAcceptDay() throws Exception {

        HttpURLConnection con = null;
        DataOutputStream wr = null;
        BufferedReader in = null;
        String json = "";
        int responseCode;
        JSONParser parser = new JSONParser();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
		String toDay = TimeUtil.getZonedDateTimeNow("Asia/Seoul").format(formatter);
        
        try{



            URL obj = new URL(url);
            
            con = (HttpURLConnection) obj.openConnection();

            con.setRequestMethod("POST");
            con.setRequestProperty("content-type", "application/json");

            Map<String, Object> sweetParam = new HashMap<String, Object>();

            sweetParam.put("tierCode", tTier);
            sweetParam.put("checkDay", toDay);


            String postJsonData = new ObjectMapper().writeValueAsString(sweetParam);

            LOGGER.debug("postJsonData:{}", postJsonData);

            // Send post request
            con.setDoOutput(true);

            wr = new DataOutputStream(con.getOutputStream());
            wr.write(postJsonData.getBytes("UTF-8"));
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

//                JSONObject res = (JSONObject) jo.get("response");
//                String resString = EntityUtils.toString(response.getEntity());

                
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
    
    
    
	
	
	
	
	//반송신청
	public JSONObject orderSweetTrackerReturn(Map<String, String> params) throws Exception {

        HttpURLConnection con = null;
        DataOutputStream wr = null;
        BufferedReader in = null;
        int responseCode;
        String json = "";
        JSONParser parser = new JSONParser();
        
        try{

            URL obj = new URL(url);
            con = (HttpURLConnection) obj.openConnection();

            con.setRequestMethod("POST");
            con.setRequestProperty("content-type", "application/json");

            Map<String, Object> sweetParam = new HashMap<String, Object>();
            Map<String, Object> dataParam = new HashMap<String, Object>();
            List <Map<String,Object>> dataList = new ArrayList<Map<String,Object>>();
            
            /*
            returnParams.put("ordCde", paramMy23OrderItem.getOrderItemId().toString());
			returnParams.put("comCode", "04");
			 //송화인
			returnParams.put("sndNme", paramMy23OrderItem.getReturnReceiverName());
			returnParams.put("sndZip", paramMy23OrderItem.getReturnReceiverPostcode());
			returnParams.put("sndAd1", paramMy23OrderItem.getReturnReceiverAddress() +" "+paramMy23OrderItem.getReturnReceiverAddressDetail() );
			returnParams.put("sndTel", paramMy23OrderItem.getReturnReceiverPhone());
            //수화인
			returnParams.put("ownNme", "테라젠이텍스바이오연구소");
			returnParams.put("ownZip", "16229");
			returnParams.put("ownAd1", "경기도 수원시 광교로 145 차세대융합기술원 A동 4층");
			returnParams.put("ownTel", "1522-2382");
			returnParams.put("wipGbn", "3");
            */
            
            
            sweetParam.put("tierCode", tTier);
            sweetParam.put("cipherType", "0");
            
            dataParam.put("ordCde", params.get("ordCde"));
            dataParam.put("comCode", params.get("comCode"));
            dataParam.put("invoice", params.get("invoice"));
            dataParam.put("cusCde", cUscde);
            //송화인
            dataParam.put("sndNme", params.get("sndNme"));
            dataParam.put("sndZip", params.get("sndZip"));
            dataParam.put("sndAd1", params.get("sndAd1"));
            dataParam.put("sndAd2", params.get("sndAd2"));
            dataParam.put("sndTel", params.get("sndTel"));
            
            //수화인
            dataParam.put("ownNme", params.get("ownNme"));
            dataParam.put("ownZip", params.get("ownZip"));
            dataParam.put("ownAd1", params.get("ownAd1"));
            dataParam.put("ownAd2", params.get("ownAd2"));
            dataParam.put("ownTel", params.get("ownTel"));
            dataParam.put("adMemo", params.get("adMemo"));
            dataParam.put("wipGbn", params.get("wipGbn"));
            dataParam.put("comDivCde", cUscde);
            dataList.add(dataParam);
            sweetParam.put("data", dataList);


            String postJsonData = new ObjectMapper().writeValueAsString(sweetParam);

            LOGGER.debug("postJsonData:{}", postJsonData);

            // Send post request
            con.setDoOutput(true);

            wr = new DataOutputStream(con.getOutputStream());
            wr.write(postJsonData.getBytes("UTF-8"));
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
    
    
    
    
    public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
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

	public String gettTier() {
		return tTier;
	}

	public void settTier(String tTier) {
		this.tTier = tTier;
	}

	public String getcUscde() {
		return cUscde;
	}

	public void setcUscde(String cUscde) {
		this.cUscde = cUscde;
	}
    
    

	
    
    
    
    
}