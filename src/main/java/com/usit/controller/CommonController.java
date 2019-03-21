package com.usit.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;


import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.usit.app.spring.exception.FrameworkException;
import com.usit.app.spring.security.domain.SignedMember;
import com.usit.app.spring.util.DateUtil;
import com.usit.app.spring.util.SessionVO;
import com.usit.app.spring.util.UsitCodeConstants;
import com.usit.app.spring.web.CommonHeaderController;
import com.usit.domain.Category;
import com.usit.domain.Member;
import com.usit.domain.SellMember;
import com.usit.domain.Unsubscribe;
import com.usit.domain.UsitCode;
import com.usit.domain.UsitEmail;
import com.usit.service.AsyncService;
import com.usit.service.CommonService;
import com.usit.service.SellMemberService;
import com.usit.util.MailUtil;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class CommonController extends CommonHeaderController{

    @Autowired
    CommonService commonService;
    
    @Autowired
    SellMemberService sellMemberService;
    
    @Autowired
    AsyncService asyncService;

    private static Logger LOGGER = LoggerFactory.getLogger(CommonController.class);

    
    /**
     * 전체코드호출
     * @param request
     * @param params
     * @return
     * @throws Exception
     */
    @GetMapping("/codes")
	public ModelAndView getCodes() {

		ModelAndView mav = new ModelAndView("jsonView");
		
		String resultCode = "0000";
        String resultMsg = "";
        
        List<UsitCode> codes = commonService.getCodes(); 

		mav.addObject("result_code", resultCode);
        mav.addObject("result_msg", resultMsg);
        mav.addObject("data", codes);
		
		 return mav;
	}
    
    
    /**
     * 상품통계 호출
     * @param request
     * @param params
     * @return
     * @throws Exception
     */
/*  봉인! @GetMapping("/codes/product-statistics")
	public ModelAndView getCategoryStatistics() {

		ModelAndView mav = new ModelAndView("jsonView");
		
		String resultCode = "0000";
        String resultMsg = "";
        
        List<Object> Statistics = commonService.getCategoryStatistics();

		mav.addObject("result_code", resultCode);
        mav.addObject("result_msg", resultMsg);
        mav.addObject("data", Statistics);
		
		 return mav;
	}
 */   
    
    /**
     * 코드호출
     * @param request
     * @param params
     * @return
     * @throws Exception
     */
    @GetMapping("/codes/master/{masterCd}")
	public ModelAndView getCodesByMasterCd(@PathVariable String masterCd) {

    		
    	ModelAndView mav = new ModelAndView("jsonView");
		
		String resultCode = "0000";
        String resultMsg = "";
        
        
        List<UsitCode> codes = commonService.getCodesByMasterCd(masterCd);

		mav.addObject("result_code", resultCode);
        mav.addObject("result_msg", resultMsg);
        mav.addObject("data", codes);
		
		 return mav;
	}
    
    
    
    
    /**
     * 카테고리호출
     * @param request
     * @param params
     * @return
     * @throws Exception
     */
    @GetMapping("/codes/category")
	public ModelAndView getCategoryCd() {

    	ModelAndView mav = new ModelAndView("jsonView");
		
		String resultCode = "0000";
        String resultMsg = "";
        
        List<Category> codes = commonService.getCategoryCd();

		mav.addObject("result_code", resultCode);
        mav.addObject("result_msg", resultMsg);
        mav.addObject("data", codes);
		
		 return mav;
	}
    

    
    
    
    
    /**
     * 택배사 조회 호출
     * @param request
     * @param params
     * @return
     * @throws Exception
     */
    @GetMapping("/tracker/delivery-company-list")
	public ModelAndView getTrackerCompany() throws Exception {

		ModelAndView mav = new ModelAndView("jsonView");
		
		String resultCode = "0000";
        String resultMsg = "";

        JSONObject result = commonService.getTrackerCompany();

		mav.addObject("result_code", resultCode);
        mav.addObject("result_msg", resultMsg);
        mav.addObject("data", result);
		
		 return mav;
	}
    
    
    /**
     * 배송상태조회 호출
     * @param request
     * @param params
     * @return
     * @throws Exception
     */
    @GetMapping("/tracker/{trackingNumber}")
	public ModelAndView getTracker(@PathVariable("trackingNumber") String trackingNumber,@RequestParam("deliveryCompanyCd") String deliveryCompanyCd) throws Exception {

		ModelAndView mav = new ModelAndView("jsonView");
		
		String resultCode = "0000";
        String resultMsg = "";

        JSONObject result = commonService.checkTracker(trackingNumber,deliveryCompanyCd);

		mav.addObject("result_code", resultCode);
        mav.addObject("result_msg", resultMsg);
        mav.addObject("data", result);
		
		 return mav;
	}
    
    
    /**
     * 영업메일 발송 호출
     * @param request
     * @param params
     * @return
     * @throws Exception
     */
    @PostMapping("/codes/mails")
	public ModelAndView sendMail(@RequestPart MultipartFile file, @RequestParam("from") String from,@RequestParam("fromName") String fromName, @RequestParam("title") String title, @RequestParam("content") String content){

		ModelAndView mav = new ModelAndView("jsonView");
		
		String resultCode = "0000";
        String resultMsg = "";
        
        SignedMember userInfo = getSignedMember(); // 로그인한 사용자의 정보를 담고 있는 객체

     	SessionVO sessionVO = userInfo.getMemberInfo(); // 로그인한 사용자의 정보로 부터 상세정보 받아옴
     	
     	SellMember seller = sellMemberService.getMemberByMemeberId(sessionVO.getMemberId());
        
     	if(!UsitCodeConstants.SELLMEMBER_TYPE_CD_MASTER.equals(seller.getMemberTypeCd())) {
     		LOGGER.warn("권한이 없습니다.");
			throw new FrameworkException("-1001", "권한이 없습니다."); // 오류 리턴 예시
     	}
        
        FileController f = new FileController();
        File excel = null;
		try {
			excel = f.convert(file);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        MailUtil mu = new MailUtil();
        ArrayList<String> address = mu.readExcel(excel);
        
        //수신거부자 제거
        List<Unsubscribe> unsubcribe = commonService.getUnsubscribeMails();        
        for (Iterator<Unsubscribe> iterator = unsubcribe.iterator(); iterator.hasNext();) {
			Unsubscribe unsubscribe = (Unsubscribe) iterator.next();
			address.remove(unsubscribe.getEmail());
		}
//        String fromName = "";
//        if("usitstorelink@usit.co.kr".equals(from)) {
//        	fromName = "storelink";
//        }else {
//        	fromName = "usit";
//        }
        asyncService.savePromotionEmails(from, fromName, address, title, content);

        
        String result = "success";
		mav.addObject("result_code", resultCode);
        mav.addObject("result_msg", resultMsg);
        mav.addObject("data", result);
		
		 return mav;
	}
    
    
    
    
    
    
    
    
    /**
     * 영업메일 발송 호출
     * @param request
     * @param params
     * @throws Exception
     */
    @Scheduled(cron = "0 10 * * * ?")
	public void sendingMail() throws Exception{

        Date Date = DateUtil.getDateFormat(DateUtil.FMT_DATE_YMD, DateUtil.getCurrDate());
        Date = DateUtil.getAddDateFormat(Date, -2);
        String sendDate = DateUtil.getDateStringFormat(DateUtil.FMT_DATE_YMD, Date);
        		
        List<UsitEmail> list = asyncService.getPromotionEmails(sendDate);
        

        asyncService.sendBatchPromotionEmails(list);
		
	}
    
    
    
    /**
     * 영업메일 발송 조회
     * @param request
     * @param params
     * @throws Exception
     */
    @GetMapping("/emails/sending")
	public ModelAndView getMail(@RequestParam("sendDate") String sendDate,@RequestParam("curPage") int curPage, @RequestParam("perPage") int perPage,@RequestParam(value="sendStatus", defaultValue = "") String sendStatus) throws Exception{

		ModelAndView mav = new ModelAndView("jsonView");
		
		String resultCode = "0000";
        String resultMsg = "";
        Pageable pageRequest = new PageRequest(curPage, perPage, Sort.Direction.DESC, "emailId");

        Page<UsitEmail> list = asyncService.getPromotionEmailsForAdmin(sendDate,sendStatus,pageRequest);
        

        mav.addObject("result_code", resultCode);
        mav.addObject("result_msg", resultMsg);
        mav.addObject("data", list);	
        return mav;
	}
    
    
    
    
    /**
     * 영업메일 수신거부 메일 저장
     * @param request
     * @param params
     * @return
     * @throws Exception
     */
    @PostMapping("/codes/unsubscribe")
	public ModelAndView receiveUnsubscribeMail(@RequestParam("email") String email) throws Exception {

		ModelAndView mav = new ModelAndView("jsonView");
		
		String resultCode = "0000";
        String resultMsg = "";
        
        Unsubscribe unsubscribe = new Unsubscribe();
        unsubscribe.setEmail(email);
        Unsubscribe data = null;
        try {
        	data = commonService.createUnsubscribe(unsubscribe);
        }catch (Exception e) {
		
        }
        
        
		mav.addObject("result_code", resultCode);
        mav.addObject("result_msg", resultMsg);
        mav.addObject("data", data);
		
		 return mav;
	}
    
    
    /**
     * 수신거부 메일 조회 호출
     * @param request
     * @param params
     * @return
     * @throws Exception
     */
    @GetMapping("/codes/unsubscribe-mails")
	public ModelAndView getUnsubscribeMails(@RequestParam("curPage") int curPage, @RequestParam("perPage") int perPage) throws Exception {

		ModelAndView mav = new ModelAndView("jsonView");
		
		String resultCode = "0000";
        String resultMsg = "";

        
        SignedMember userInfo = getSignedMember(); // 로그인한 사용자의 정보를 담고 있는 객체

     	SessionVO sessionVO = userInfo.getMemberInfo(); // 로그인한 사용자의 정보로 부터 상세정보 받아옴
     	
     	SellMember seller = sellMemberService.getMemberByMemeberId(sessionVO.getMemberId());
        
     	if(!UsitCodeConstants.SELLMEMBER_TYPE_CD_MASTER.equals(seller.getMemberTypeCd())) {
     		LOGGER.warn("권한이 없습니다.");
			throw new FrameworkException("-1001", "권한이 없습니다."); // 오류 리턴 예시
     	}
     	List<Unsubscribe> page = null;
//        Pageable pageRequest = new PageRequest(curPage, perPage, Sort.Direction.DESC, "email");

//        Page<Unsubscribe> page = null;
        try {

            page = commonService.getUnsubscribeMails();
        
        }catch(Exception e){
            logger.error("Exception", e);
            resultCode = "-9999";
            resultMsg = "처리중 오류가 발생하였습니다.";
        }

		mav.addObject("result_code", resultCode);
        mav.addObject("result_msg", resultMsg);
        mav.addObject("data", page);
		
		 return mav;
	}
    
    
    
    
    
    
    
    /**
     * 프론트 버전관리 조회
     * @param request
     * @param params
     * @return
     * @throws Exception
     */
    @GetMapping("/codes/version/{masterCd}")
	public ModelAndView getVersion(@PathVariable String masterCd) throws Exception {

		ModelAndView mav = new ModelAndView("jsonView");
		
		String resultCode = "0000";
        String resultMsg = "";

        List<UsitCode> codes = null;
        
     	
   		codes = commonService.getCodesByMasterCd(masterCd);

		mav.addObject("result_code", resultCode);
        mav.addObject("result_msg", resultMsg);
        mav.addObject("data", codes);
		
		 return mav;
	}
    
    
    
    /**
     * 프론트 버전관리 수정
     * @param request
     * @param params
     * @return
     * @throws Exception
     */
    @PutMapping("/codes/version")
	public ModelAndView putVersion(@RequestParam ("version") String version) throws Exception {

		ModelAndView mav = new ModelAndView("jsonView");
		
		String resultCode = "0000";
        String resultMsg = "";

        UsitCode code = null;
        
        SignedMember userInfo = getSignedMember(); // 로그인한 사용자의 정보를 담고 있는 객체
        
        SessionVO sessionVO = userInfo.getMemberInfo(); // 로그인한 사용자의 정보로 부터 상세정보 받아옴
     	if(UsitCodeConstants.USIT_MASTER_EMAIL_ADDRESS.equals(sessionVO.getMemberEmail())) {
     		code = commonService.PutCurrentVersion(UsitCodeConstants.USIT_CODE_FRONT_DETAIL_CD,version);
     	}else {
     		LOGGER.warn("권한이 없습니다.");
			throw new FrameworkException("-1001", "권한이 없습니다."); // 오류 리턴 예시
     	}

		mav.addObject("result_code", resultCode);
        mav.addObject("result_msg", resultMsg);
        mav.addObject("data", code);
		
		 return mav;
	}
    
    
    
    
    

}
