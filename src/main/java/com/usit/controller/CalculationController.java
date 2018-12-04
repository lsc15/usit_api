package com.usit.controller;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
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
import com.usit.util.TimeUtil;

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
        
        
     	int data = calculationService.createCalculation(calculation);
		
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
	public ResponseEntity<JSONObject> getCalculation(@RequestParam("curPage") int curPage, @RequestParam("perPage") int perPage,
			   @RequestParam("periodCondition") String periodCondition,@RequestParam("startDate") String startDate,@RequestParam("endDate") String endDate,@RequestParam(name = "sellMemberId", required = false) Integer sellMemberId) throws UnsupportedEncodingException, NoSuchAlgorithmException, GeneralSecurityException {

//    	@RequestParam("curPage") int curPage, @RequestParam("perPage") int perPage,
//		   @RequestParam("periodCondition") String periodCondition,@RequestParam("startDate") String startDate,@RequestParam("endDate") String endDate,
//		   @RequestParam(value="keywordCondition", defaultValue = "") String keywordCondition,@RequestParam(value="keyword", defaultValue = "") String keyword
    	
    	PageRequest pageRequest = new PageRequest(curPage, perPage, new Sort(Direction.DESC, "calculationId"));
//   		ModelAndView mav = new ModelAndView("jsonView");
		
   		JSONObject jo = new JSONObject();
		
		
   		
		String resultCode = "0000";
        String resultMsg = "";
        
        SignedMember userInfo = getSignedMember(); // 로그인한 사용자의 정보를 담고 있는 객체

     	SessionVO sessionVO = userInfo.getMemberInfo(); // 로그인한 사용자의 정보로 부터 상세정보 받아옴
     	
     	SellMember seller = sellMemberService.getMemberByMemeberId(sessionVO.getMemberId());
        
     	if(!UsitCodeConstants.SELLMEMBER_TYPE_CD_MASTER.equals(seller.getMemberTypeCd())) {
     		LOGGER.warn("권한이 없습니다.");
			throw new FrameworkException("-1001", "권한이 없습니다."); // 오류 리턴 예시
     	}
     	
     	Page<Calculation> page;
     	//판매자아이디 분기.
     	if(sellMemberId != null && sellMemberId != 0 ) {
     		page = calculationService.readAllByToken(pageRequest,periodCondition,startDate,endDate,sellMemberId);
     	}else {
     		page = calculationService.readAll(pageRequest,periodCondition,startDate,endDate);
     	}
     	
     	
		
//		mav.addObject("result_code", resultCode);
//        mav.addObject("result_msg", resultMsg);
//        mav.addObject("data", page);
     	jo.put("result_code", "0000");
		jo.put("result_msg", "정상 처리되었습니다.");
		jo.put("data", page);
     	ResponseEntity<JSONObject> response = new ResponseEntity<JSONObject>(jo, HttpStatus.OK);
     	
		
		 return response;
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
 	 @Scheduled(cron = "0 10 21 * * ?")
 	 @Transactional(propagation=Propagation.REQUIRES_NEW)
     public void calculate() throws Exception{
     	if("real".equals(env.getProperty("running.system"))) {
     	logger.info("@@일일정산 시작");
     	List<UsitOrderItem> calculationList = orderItemService.getCaculationByDeliveryStatusCd(UsitCodeConstants.DELIVERY_STATUS_CD_DELIVERY_COMPLETE);
     	
     	for (Iterator<UsitOrderItem> iterator = calculationList.iterator(); iterator.hasNext();) {
 			UsitOrderItem usitOrderItem = (UsitOrderItem) iterator.next();
 			Calculation cal = new Calculation();
 			Calculation deliveryCal = new Calculation();
 			// 정산데이타 입력
 			/**
 			 * 오늘의 주차를가져오고 1주차 수요일 보다 작거나 같으면 1주차 수요일 크면 3주차 수요일 그보다 크면 다음달 1주차 수요일의 일자를 예정일로 가져온다
 			 */
 			
 			Date current = DateUtil.getDateFormat(DateUtil.FMT_DATE_YMD,DateUtil.getCurrDate());
 			Date dueDate = DateUtil.getDateFormat(DateUtil.FMT_DATE_YMD,DateUtil.getCurrDate("YYYYMM")+"01");
 			Date first = DateUtil.getCalFirstWeekWedDayOfMonthTsst(dueDate);
 			Date third = DateUtil.getAddDateFormat(first, 14);
 			Date temp = DateUtil.getAddMonthFormat(dueDate, 1);
 			Date nextFirst = DateUtil.getCalFirstWeekWedDayOfMonthTsst(temp);

 			String calculationDueDate;
 			
 			int resultFirst = first.compareTo(current);
 			int resultThird = third.compareTo(current);
 			
 			if(resultFirst >= 0) {
 				calculationDueDate = DateUtil.getDateStringFormat(DateUtil.FMT_DATE_YMD, first);
 			}else if (resultThird >= 0) {
 				calculationDueDate = DateUtil.getDateStringFormat(DateUtil.FMT_DATE_YMD, third);
 			}else {
 				calculationDueDate = DateUtil.getDateStringFormat(DateUtil.FMT_DATE_YMD, nextFirst);
 			}
 			
 			cal.setCalculationDueDate(calculationDueDate);
 			cal.setTypeCd(UsitCodeConstants.CACULATION_TYEP_CD_PURCHASE);
 			cal.setPurchaseConfirmDate(DateUtil.getCurrDate());
 			cal.setOrderItemId(usitOrderItem.getOrderItemId());
 			cal.setAmount(usitOrderItem.getAmount());
 			cal.setRegId(0);
 			cal.setRegDate(TimeUtil.getZonedDateTimeNow("Asia/Seoul"));
 			cal.setSellMemberId(usitOrderItem.getSellMemberId());
 			cal.setStatusCd(UsitCodeConstants.CACULATION_STATUS_CD_STANDBY);
 			int result = calculationService.createCalculation(cal);
 			logger.info("@@save purchase : " +result);
 			
 			
 			deliveryCal.setCalculationDueDate(calculationDueDate);
 			deliveryCal.setTypeCd(UsitCodeConstants.CACULATION_TYEP_CD_DELIVERY);
 			deliveryCal.setPurchaseConfirmDate(DateUtil.getCurrDate());
 			deliveryCal.setOrderItemId(usitOrderItem.getOrderItemId());
 			deliveryCal.setAmount(usitOrderItem.getOrder().getDeliveryAmount());
 			deliveryCal.setRegId(0);
 			deliveryCal.setRegDate(TimeUtil.getZonedDateTimeNow("Asia/Seoul"));
 			deliveryCal.setSellMemberId(usitOrderItem.getSellMemberId());
 			deliveryCal.setStatusCd(UsitCodeConstants.CACULATION_STATUS_CD_STANDBY);
 			result = calculationService.createCalculation(deliveryCal);
 			logger.info("@@save delivery : "+result);
 			
 			
 		}
     	logger.info("@@일일정산 완료건: "+ calculationList.size());
     	}
     }
    
}
