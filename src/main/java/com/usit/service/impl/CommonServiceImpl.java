package com.usit.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.usit.domain.Category;
import com.usit.domain.UsitCode;
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
    private Environment env;
    
    @Autowired
    private UsitCodeRepository usitCodeRepository;
    
    @Autowired
    private CategoryRepository categoryRepository;
    
    @Autowired
    private EntityManager em;

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
