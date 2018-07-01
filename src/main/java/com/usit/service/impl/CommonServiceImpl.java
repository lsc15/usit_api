package com.usit.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.usit.domain.Category;
import com.usit.domain.UsitCode;
import com.usit.app.util.messenger.alimtalk.AlimTalkSender;
import com.usit.domain.AlimtalkMessage;
import com.usit.repository.AlimtalkMessageRepository;
import com.usit.app.spring.service.CommonHeaderService;
import com.usit.app.util.messenger.sms.SmsSender;
import com.usit.app.util.sweettracker.TrackerSender;
import com.usit.domain.VerifyToken;
import com.usit.repository.CategoryRepository;
import com.usit.repository.UsitCodeRepository;
import com.usit.repository.VerifyTokenRepository;
import com.usit.service.CommonService;
import com.usit.util.EtcUtil;

@Service
public class CommonServiceImpl extends CommonHeaderService implements CommonService {

    @Autowired
    private VerifyTokenRepository verifyTokenRepository;
    
    
    @Autowired
    private SmsSender smsSender;
    
    @Autowired
    private TrackerSender trackerSender;
    
    @Autowired
    private AlimTalkSender alimTalkSender;
    
    @Autowired
    private Environment env;
    
    @Autowired
    private UsitCodeRepository usitCodeRepository;
    
    @Autowired
    private CategoryRepository categoryRepository;
    
    @Autowired
    private EntityManager em;

    @Autowired
    private AlimtalkMessageRepository alimtalkMessageRepository;
    
//    @Override
//    public VerifyToken putCertList(VerifyToken verifyToken) throws Exception{
//        //난수생성변수
//        int iSecureRand = 0;
//
//        String regId = "";
//
//        if(getSignedMember() != null) {
//            regId = getSignedMember().getUserId();
//        }
//
//        // 난수생성
//        iSecureRand = EtcUtil.getRandomNum(100000, 999999); // 6자리 생성
//
//        verifyToken.setToken(Integer.toString(iSecureRand));
////        verifyToken.setToken("123456"); // 문자 발송 때까지 임시로 고정 번호 셋팅(for TEST)
//        verifyToken.setType("mobile");
//        verifyToken.setRegId(regId);
//
//        verifyTokenRepository.save(verifyToken);
//
//        Map<String, Object> paramMap = new HashMap<String, Object>();
//
//        String msg = "usit 인증번호는 [" + iSecureRand + "] 입니다"; // TODO: 문구 DB 템플릿화 고려하기
//        paramMap.put("title", "usit 인증번호"); // 제목
//        paramMap.put("text", msg); // 보낼내용
//        paramMap.put("to", verifyToken.getMobile()); // 받는휴대폰 번호( '-' 문자 포함)
//
//        smsSender.setApiUrlForToken(env.getProperty("sms.infobank.rest.url_for_token"));
//        smsSender.setApiUrlForRequest(env.getProperty("sms.infobank.rest.url_for_request"));
//        smsSender.setUserId(env.getProperty("sms.infobank.rest.id"));
//        smsSender.setUserPwd(env.getProperty("sms.infobank.rest.password"));
//
//        
//        //	TODO 문자 발송 API 연동
//        smsSender.send(paramMap);
//
//        return verifyToken;
//    }

    
    @Override
    public VerifyToken getCertInfo(VerifyToken verifiyToken) throws Exception {
        int seqId = verifiyToken.getVerifyTokenId();
        VerifyToken verifyToken = verifyTokenRepository.findOne(seqId);
        if(verifyToken == null) {
            return null;
        }
        VerifyToken lastVerifyToken = verifyTokenRepository.findFirstByMobileOrderByVerifyTokenIdDesc(verifyToken.getMobile());

        logger.debug("lastVerifyToken.getVerifyTokenId:{}", lastVerifyToken.getVerifyTokenId());
        logger.debug("lastVerifyToken.getToken:{}", lastVerifyToken.getToken());


        return lastVerifyToken;
    }
    
    
    
    
    public List<UsitCode> getCodes() {
		List<UsitCode> codes = usitCodeRepository.findAll();

    logger.debug("codes.size()", codes.size());


    return codes;
}
    
    
    public List<Object> getCategoryStatistics() {
		
    	List<UsitCode> codes = usitCodeRepository.findByMasterCd("02");

    logger.debug("codes.size()", codes.size());
    
    
    List<Object> result = new ArrayList<Object>();
    
    
//    TypedQuery<String> countQuery = em.createQuery("" +
    Query countQuery = em.createQuery("" +
            "SELECT p.categoryCd,count(p.categoryCd) " +
            "FROM Product p " +
            "GROUP BY p.categoryCd ");

      List<Object[]> sum = countQuery.getResultList();
//      HashMap<String, String> map = new HashMap<>();
//      for (Object[] a : authors) {
//    	  map.put(String.valueOf(a[0]), String.valueOf(a[1]));
//      }
      
      
      int size = sum.size();
      for (int i = 0; i < size; i++) {
    	  HashMap<String, String> map = new HashMap<>();
    	  
    	  
    	  
    	  map.put("masterCd", codes.get(i).getMasterCd());
    	  map.put("masterCdNm", codes.get(i).getMasterCdNm());
    	  map.put("detailCd", codes.get(i).getDetailCd());
    	  map.put("detailCdNm", codes.get(i).getDetailCdNm());
    	  map.put("useYn", codes.get(i).getUseYn());
    	  map.put("productCnt", String.valueOf(sum.get(i)[1]));
		
    	  result.add(map);
	}
      
   
    return result;
}





public List<UsitCode> getCodesByMasterCd(String masterCd) {
    List<UsitCode> codes = usitCodeRepository.findByMasterCd(masterCd);
    
    logger.debug("codes.size()", codes.size());


    return codes;
}
    
    




/**
 * 택배사코드 조회
 * @param UsitOrderItem
 * @return HTTPCODE
 * @throws Exception
 */
public JSONObject getTrackerCompany() throws Exception{
    

	trackerSender.setUrl(env.getProperty("sweettracker.rest.companylist.url"));
	trackerSender.settKey(env.getProperty("sweettracker.rest.api_key"));
	
    return trackerSender.sendCompanylistApi();
}

/**
 * @param UsitOrderItem
 * @return HTTPCODE
 * @throws Exception
 */
public JSONObject checkTracker(String trackingNumber) throws Exception{
    

	trackerSender.setUrl(env.getProperty("sweettracker.rest.url"));
	trackerSender.settKey(env.getProperty("sweettracker.rest.api_key"));
	trackerSender.settCode(env.getProperty("sweettracker.rest.api_company_code"));
	
	
    return trackerSender.send(trackingNumber);
}



/**
 * @param UsitOrderItem
 * @return HTTPCODE
 * @throws Exception
 */
public JSONObject checkSweetTrackerReturnAcceptDay() throws Exception{
    

	trackerSender.setUrl(env.getProperty("sweettracker.rest.return.check.url"));
	trackerSender.settTier(env.getProperty("sweettracker.rest.tier"));
	
	
    return trackerSender.checkSweetTrackerReturnAcceptDay();
}




/**
 * @param UsitOrderItem
 * @return HTTPCODE
 * @throws Exception
 */
public JSONObject orderSweetTrackerReturn(Map<String, String> params) throws Exception{
    
if("real".equals(params.get("mode"))) {
	trackerSender.setUrl(env.getProperty("sweettracker.rest.return.order.url"));
}else {
	trackerSender.setUrl(env.getProperty("sweettracker.rest.return.order.devurl"));
}
	trackerSender.settTier(env.getProperty("sweettracker.rest.tier"));
	trackerSender.setcUscde(env.getProperty("sweettracker.rest.cuscde"));
	
    return trackerSender.orderSweetTrackerReturn(params);
}







/**
 * @param templateCode
 * @param variable[]
 * @param find variable list in alimtalk_message Table
 * @return HTTPCODE
 * @throws Exception
 */
public int sendAlimtalk(String templateCd,String mobile,String [] variable) throws Exception{
    
	alimTalkSender.setUrl(env.getProperty("kakao.alimtalk.url"));
	alimTalkSender.setUserId(env.getProperty("kakao.alimtalk.id"));
	alimTalkSender.setUserPwd(env.getProperty("kakao.alimtalk.password"));
	alimTalkSender.setMsgType(env.getProperty("kakao.alimtalk.msg_type"));
	alimTalkSender.setMtFailover(env.getProperty("kakao.alimtalk.mt_failover"));
	alimTalkSender.setSenderKey(env.getProperty("kakao.alimtalk.sender_key"));
	alimTalkSender.setResponseMethod(env.getProperty("kakao.alimtalk.response_method"));
	alimTalkSender.setSenderId(env.getProperty("kakao.alimtalk.sender_id"));
	Map<String, String> paramMap = new HashMap<String, String>();

	AlimtalkMessage kakao = alimtalkMessageRepository.findByTemplateCd(templateCd);

	String content = makeContent(kakao.getContent(), variable);

	paramMap.put("to", mobile);

	paramMap.put("content", content);

	paramMap.put("template_code", templateCd);

	return alimTalkSender.send(paramMap);

}


	/**
 	* @param 
 	* @param 
 	* @return 카테고리 테이블
 	*/
	public List<Category> getCategoryCd() {
		
		List<Category> category = categoryRepository.findAll();
		return category;
	}
    
    /**
     * @param content
     * @param variable
     * @return 템플릿에 변수를 대입한문장 반환
     */
    public String makeContent(String content,String [] variable) {
    	for (String str : variable) {
    		content = content.replaceFirst("#", str);
		}
    	//이런된장같은 한시간 걸렸네
    		content=content.replaceAll("\\\\r\\\\n", "\r\n");
    	return content;
    }

    
}
