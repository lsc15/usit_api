package com.usit.controller;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.usit.app.spring.exception.FrameworkException;
import com.usit.app.spring.security.domain.SignedMember;
import com.usit.app.spring.util.DateUtil;
import com.usit.app.spring.util.SessionVO;
import com.usit.app.spring.util.UsitCodeConstants;
import com.usit.app.spring.web.CommonHeaderController;
import com.usit.domain.Member;
import com.usit.domain.SellMember;
import com.usit.domain.WithdrawRequest;
import com.usit.service.CommonService;
import com.usit.service.MemberService;
import com.usit.service.SellMemberService;
import com.usit.service.WithDrawRequestService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/withdraw-requests")
public class WithdrawRequestController extends CommonHeaderController{


    @Autowired
    WithDrawRequestService withdrawRequestService;
    
    @Autowired
    private CommonService commonService;
    
    @Autowired
    SellMemberService sellMemberService;
    
    @Autowired
    MemberService memberService;
    
    private static Logger LOGGER = LoggerFactory.getLogger(WithdrawRequestController.class);

    
    /**
     * 출금요청 저장
     * @param request
     * @param params
     * @return 
     * @throws Exception
     */
    @PostMapping
	public ModelAndView saveWithDrawRequest(@RequestBody WithdrawRequest withdrawRequest) throws Exception {

   		ModelAndView mav = new ModelAndView("jsonView");
		
		String resultCode = "0000";
        String resultMsg = "";
        
        
        /**
         * 출금 요청검증
         * 사용자가 보유중인 포인트와 히스토리에 모든 addPoint 비교
         * 
         */
        
        SignedMember userInfo = getSignedMember(); // 로그인한 사용자의 정보를 담고 있는 객체

     	SessionVO sessionVO = userInfo.getMemberInfo(); // 로그인한 사용자의 정보로 부터 상세정보 받아옴
        
        Member member = memberService.getMemberByMemeberId(withdrawRequest.getMemberId());
        withdrawRequest.setRegId(sessionVO.getMemberId());
        if(withdrawRequest.getAmount() > member.getWithdrawablePoint()) {
        	LOGGER.warn("보유금액 이상의 출금요청을 할 수 없습니다.");
			throw new FrameworkException("-1001", "보유금액 이상의 포인트를 출금요청 할 수 없습니다.");
        }
        
     	WithdrawRequest withdrawRequst = new WithdrawRequest();
     	withdrawRequst = withdrawRequestService.createWithdrawRequest(withdrawRequest);
     	
     	
     	
    	/**
    	  * 
   	  	  * #{고객명},#{출금신청포인트},#{포인트잔액},#{포인트입금예상일},#{포인트입금예상일}
		  *
		  */
     	
     	String variable [] = new String [5];
     	variable[0] = withdrawRequst.getOwnerName();
     	variable[1] = String.valueOf(withdrawRequst.getAmount());
     	variable[2] = String.valueOf(member.getTotalPoint());

     	
     	
		Date date = DateUtil.getDateFormat(DateUtil.FMT_DATE_YMD,DateUtil.getCurrDate());
		String diffDay = "";
		//일월화수목금토
		if(DateUtil.getWeekday(date) < 4) {
			diffDay = DateUtil.doDiffOfDate(DateUtil.getDateStringFormat( DateUtil.FMT_DATE_YMD,date),DateUtil.getDateStringFormat(DateUtil.FMT_DATE_YMD,DateUtil.getWeekFridayDate(date)));
		}else {
			diffDay = DateUtil.doDiffOfDate(DateUtil.getDateStringFormat( DateUtil.FMT_DATE_YMD,date),DateUtil.getDateStringFormat(DateUtil.FMT_DATE_YMD,DateUtil.getWeekNextFridayDate(date)));
		}
     	variable[3] = diffDay;
     	variable[4] = diffDay;
   	  
     	int status = commonService.sendAlimtalk("U024",member.getMobile(),variable);
     	logger.info("kakaoStatus : "+status);
		
		mav.addObject("result_code", resultCode);
        mav.addObject("result_msg", resultMsg);
        mav.addObject("data", withdrawRequst);
		
		 return mav;
	}
    
    
    
    
    
    /**
     * 관리자 출금요청내역 조회
     * @param request
     * @param params
     * @return 
     * @throws GeneralSecurityException 
     * @throws NoSuchAlgorithmException 
     * @throws UnsupportedEncodingException 
     * @throws Exception
     */
    @GetMapping()
	public ModelAndView getWithDrawRequest(@RequestParam("curPage") int curPage, @RequestParam("perPage") int perPage,
			   @RequestParam("periodCondition") String periodCondition,@RequestParam("startDate") String startDate,@RequestParam("endDate") String endDate,
			   @RequestParam(value="keywordCondition", defaultValue = "") String keywordCondition,@RequestParam(value="keyword", defaultValue = "") String keyword) throws UnsupportedEncodingException, NoSuchAlgorithmException, GeneralSecurityException {

//    	@RequestParam("curPage") int curPage, @RequestParam("perPage") int perPage,
//		   @RequestParam("periodCondition") String periodCondition,@RequestParam("startDate") String startDate,@RequestParam("endDate") String endDate,
//		   @RequestParam(value="keywordCondition", defaultValue = "") String keywordCondition,@RequestParam(value="keyword", defaultValue = "") String keyword
    	
    	PageRequest pageRequest = new PageRequest(curPage, perPage, new Sort(Direction.DESC, "withdrawRequestId"));
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
        
     	Page<WithdrawRequest> page = withdrawRequestService.readAll(pageRequest,periodCondition,startDate,endDate,keywordCondition,keyword);
		
		mav.addObject("result_code", resultCode);
        mav.addObject("result_msg", resultMsg);
        mav.addObject("data", page);
		
		 return mav;
	}
    
    
    
    
    
    
    /**
     * 나의 출금요청내역 조회
     * @param request
     * @param params
     * @return 
     * @throws GeneralSecurityException 
     * @throws NoSuchAlgorithmException 
     * @throws UnsupportedEncodingException 
     * @throws Exception
     */
    @GetMapping("/token")
	public ModelAndView getMyWithDrawRequest(HttpServletRequest request, @RequestParam("curPage") int curPage, @RequestParam("perPage") int perPage) throws UnsupportedEncodingException, NoSuchAlgorithmException, GeneralSecurityException {

    	PageRequest pageRequest = new PageRequest(curPage, perPage, new Sort(Direction.DESC, "withdrawRequestId"));
   		ModelAndView mav = new ModelAndView("jsonView");
		
		String resultCode = "0000";
        String resultMsg = "";
        
        SignedMember userInfo = getSignedMember(); // 로그인한 사용자의 정보를 담고 있는 객체

     	SessionVO sessionVO = userInfo.getMemberInfo(); // 로그인한 사용자의 정보로 부터 상세정보 받아옴
        
     	Page<WithdrawRequest> page = withdrawRequestService.readAllByToken(pageRequest,sessionVO.getMemberId());
		
		mav.addObject("result_code", resultCode);
        mav.addObject("result_msg", resultMsg);
        mav.addObject("data", page);
		
		 return mav;
	}
    
    
    
    /**
     * 출금요청내역 수정
     * @param request
     * @param params
     * @return 
     * @throws Exception
     */
    @PutMapping("/{withdrawRequestId}")
	public ModelAndView modifyWithDrawRequest(@RequestBody WithdrawRequest withdrawRequest,@PathVariable int withdrawRequestId) {

   		ModelAndView mav = new ModelAndView("jsonView");
		
		String resultCode = "0000";
        String resultMsg = "";

        
        SignedMember userInfo = getSignedMember(); // 로그인한 사용자의 정보를 담고 있는 객체

     	SessionVO sessionVO = userInfo.getMemberInfo(); // 로그인한 사용자의 정보로 부터 상세정보 받아옴
        
     	withdrawRequest.setModId(sessionVO.getMemberId());
        
     	WithdrawRequest result = withdrawRequestService.modifyWithdrawRequest(withdrawRequest);
		
		mav.addObject("result_code", resultCode);
        mav.addObject("result_msg", resultMsg);
        mav.addObject("data", result);
		
		 return mav;
	}
    
}
