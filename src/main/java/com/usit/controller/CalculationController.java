package com.usit.controller;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.scheduling.annotation.Scheduled;
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
import com.usit.app.spring.util.SessionVO;
import com.usit.app.spring.util.UsitCodeConstants;
import com.usit.app.spring.web.CommonHeaderController;
import com.usit.domain.Calculation;
import com.usit.domain.Member;
import com.usit.domain.SellMember;
import com.usit.domain.UsitOrderItem;
import com.usit.domain.WithdrawRequest;
import com.usit.service.CalculationService;
import com.usit.service.MemberService;
import com.usit.service.OrderItemService;
import com.usit.service.SellMemberService;
import com.usit.service.WithDrawRequestService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/calculations")
public class CalculationController extends CommonHeaderController{


    @Autowired
    CalculationService calculationService;
    
    @Autowired
    SellMemberService sellMemberService;
    
    @Autowired
    private OrderItemService orderItemService;
    
    @Autowired
    private Environment env;
    
    private static Logger LOGGER = LoggerFactory.getLogger(CalculationController.class);

    
    /**
     * 정산 저장
     * @param request
     * @param params
     * @return 
     * @throws GeneralSecurityException 
     * @throws NoSuchAlgorithmException 
     * @throws UnsupportedEncodingException 
     * @throws Exception
     */
    @PostMapping
	public ModelAndView saveCalculation(@RequestBody Calculation calculation) {

   		ModelAndView mav = new ModelAndView("jsonView");
		
		String resultCode = "0000";
        String resultMsg = "";
        
        
        SignedMember userInfo = getSignedMember(); // 로그인한 사용자의 정보를 담고 있는 객체

     	SessionVO sessionVO = userInfo.getMemberInfo(); // 로그인한 사용자의 정보로 부터 상세정보 받아옴
        
        
     	calculation = calculationService.createCalculation(calculation);
		
		mav.addObject("result_code", resultCode);
        mav.addObject("result_msg", resultMsg);
        mav.addObject("data", calculation);
		
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
	public ModelAndView getCalculation(@RequestParam("curPage") int curPage, @RequestParam("perPage") int perPage,
			   @RequestParam("periodCondition") String periodCondition,@RequestParam("startDate") String startDate,@RequestParam("endDate") String endDate) throws UnsupportedEncodingException, NoSuchAlgorithmException, GeneralSecurityException {

//    	@RequestParam("curPage") int curPage, @RequestParam("perPage") int perPage,
//		   @RequestParam("periodCondition") String periodCondition,@RequestParam("startDate") String startDate,@RequestParam("endDate") String endDate,
//		   @RequestParam(value="keywordCondition", defaultValue = "") String keywordCondition,@RequestParam(value="keyword", defaultValue = "") String keyword
    	
    	PageRequest pageRequest = new PageRequest(curPage, perPage, new Sort(Direction.DESC, "calculationId"));
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
        
     	Page<Calculation> page = calculationService.readAll(pageRequest,periodCondition,startDate,endDate);
		
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
	public ModelAndView getMyCalculation(HttpServletRequest request, @RequestParam("curPage") int curPage, @RequestParam("perPage") int perPage,
			@RequestParam("periodCondition") String periodCondition,@RequestParam("startDate") String startDate, @RequestParam("endDate") String endDate) throws UnsupportedEncodingException, NoSuchAlgorithmException, GeneralSecurityException {

    	PageRequest pageRequest = new PageRequest(curPage, perPage, new Sort(Direction.DESC, "calculationId"));
   		ModelAndView mav = new ModelAndView("jsonView");
		
		String resultCode = "0000";
        String resultMsg = "";
        
        SignedMember userInfo = getSignedMember(); // 로그인한 사용자의 정보를 담고 있는 객체

     	SessionVO sessionVO = userInfo.getMemberInfo(); // 로그인한 사용자의 정보로 부터 상세정보 받아옴
        
     	Page<Calculation> page = calculationService.readAllByToken(pageRequest,periodCondition,startDate,endDate,sessionVO.getMemberId());
		
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
    @PutMapping("/{calculationId}")
	public ModelAndView modifyWithDrawRequest(@RequestBody Calculation calculation,@PathVariable int calculationId) {

   		ModelAndView mav = new ModelAndView("jsonView");
		
		String resultCode = "0000";
        String resultMsg = "";

        
        SignedMember userInfo = getSignedMember(); // 로그인한 사용자의 정보를 담고 있는 객체

     	SessionVO sessionVO = userInfo.getMemberInfo(); // 로그인한 사용자의 정보로 부터 상세정보 받아옴
        
     	calculation.setModId(sessionVO.getMemberId());
        
     	Calculation result = calculationService.modifyCalculation(calculation);
		
		mav.addObject("result_code", resultCode);
        mav.addObject("result_msg", resultMsg);
        mav.addObject("data", result);
		
		 return mav;
	}
    
    
    
    
    
    // 정산테이블 배치 초 분 시 일 월 주(년)
// 	@Scheduled(cron = "0 10 21 * * ?")
     public void updateReturnDeliveryStatus() throws Exception{
     	if("real".equals(env.getProperty("running.system"))) {
     	logger.info("@@일일정산 시작");
     	List<UsitOrderItem> culationList = orderItemService.getCaculationByDeliveryStatusCd(UsitCodeConstants.DELIVERY_STATUS_CD_DELIVERY_COMPLETE);
     	  
     	for (Iterator<UsitOrderItem> iterator = culationList.iterator(); iterator.hasNext();) {
 			UsitOrderItem usitOrderItem = (UsitOrderItem) iterator.next();
 			Calculation cal = new Calculation();
 			// 정산데이타 입력
 			
 			
 			
 			
 			logger.info("@@save ");
 			
 		}
     	logger.info("@@일일정산 완료건: "+ culationList.size());
     	}
     }
    
}
