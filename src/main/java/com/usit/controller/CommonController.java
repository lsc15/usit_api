package com.usit.controller;

import java.io.File;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.HtmlUtils;

import com.usit.app.spring.exception.FrameworkException;
import com.usit.app.spring.security.domain.SignedMember;
import com.usit.app.spring.ui.dto.ComUiDTO;
import com.usit.app.spring.util.SessionVO;
import com.usit.app.spring.util.UsitCodeConstants;
import com.usit.app.spring.web.CommonHeaderController;
import com.usit.domain.Category;
import com.usit.domain.Member;
import com.usit.domain.PostingHistory;
import com.usit.domain.SellMember;
import com.usit.domain.UsitCode;
import com.usit.domain.VerifyToken;
import com.usit.service.AsyncService;
import com.usit.service.CommonService;
import com.usit.service.PostingHistoryService;
import com.usit.service.SellMemberService;
import com.usit.util.InstagramUtil;
import com.usit.util.MailUtil;
import com.usit.util.TimeUtil;

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
	public ModelAndView sendMail(@RequestPart MultipartFile file, @RequestParam("title") String title, @RequestParam("content") String content) throws Exception {

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
        File excel = f.convert(file);

        MailUtil mu = new MailUtil();
        ArrayList<String> address = mu.readExcel(excel);
        
        
        asyncService.sendPromotionEmails(address,title,content);

        
        String result = "success";
		mav.addObject("result_code", resultCode);
        mav.addObject("result_msg", resultMsg);
        mav.addObject("data", result);
		
		 return mav;
	}

}
