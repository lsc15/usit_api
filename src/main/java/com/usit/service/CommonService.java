package com.usit.service;


import java.util.List;
import java.util.Map;

import org.json.simple.JSONObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.usit.domain.Category;
import com.usit.domain.Inquiry;
import com.usit.domain.Unsubscribe;
import com.usit.domain.UsitCode;
import com.usit.domain.VerifyToken;

public interface CommonService {

	
	public List<UsitCode> getCodes();
	public List<Object> getCategoryStatistics();
    public List<UsitCode> getCodesByMasterCd(String masterCd);
    public List<Category> getCategoryCd();
//    public VerifyToken putCertList(VerifyToken verifyToken) throws Exception;
    public int sendAlimtalk(String templateCd,String mobile,String [] variable) throws Exception;
    public VerifyToken getCertInfo(VerifyToken verifyToken) throws Exception;
    public JSONObject getTrackerCompany() throws Exception;
    public JSONObject checkTracker(String trackingNumber, String deliveryCompanyCd) throws Exception;
    public JSONObject checkSweetTrackerReturnAcceptDay() throws Exception;
    public JSONObject orderSweetTrackerReturn(Map<String, String> params) throws Exception;
    
    public List<Unsubscribe> getUnsubscribeMails();
    public Unsubscribe createUnsubscribe(Unsubscribe unsubscribe);
    public UsitCode PutCurrentVersion(String detailCd, String version);
    
    
    
}
