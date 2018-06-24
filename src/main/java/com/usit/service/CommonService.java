package com.usit.service;


import java.util.List;

import org.json.simple.JSONObject;

import com.usit.domain.Category;
import com.usit.domain.UsitCode;
import com.usit.domain.VerifyToken;

public interface CommonService {

	
	public List<UsitCode> getCodes();
	public List<Object> getCategoryStatistics();
    public List<UsitCode> getCodesByMasterCd(String masterCd);
    public List<Category> getCategoryCd();
//    public VerifyToken putCertList(VerifyToken verifyToken) throws Exception;
    public VerifyToken getCertInfo(VerifyToken verifyToken) throws Exception;
    
}
