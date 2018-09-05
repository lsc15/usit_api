package com.usit.controller;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import com.usit.app.spring.exception.FrameworkException;
import com.usit.app.spring.security.domain.SignedMember;
import com.usit.app.spring.ui.dto.ComUiDTO;
import com.usit.app.spring.util.UsitCodeConstants;
import com.usit.app.spring.web.CommonHeaderController;
import com.usit.domain.UsitOrder;
import com.usit.domain.UsitOrderItem;
import com.usit.service.CommonService;
import com.usit.service.OrderItemService;
import com.usit.service.OrderService;
import com.usit.util.EtcUtil;
import com.usit.util.TimeUtil;
import lombok.RequiredArgsConstructor;

/**
 * 주문 아이템 관리 컨트롤러
 *
 */
@RestController
@RequiredArgsConstructor
public class UsitOrderItemController extends CommonHeaderController{

	
	private static Logger LOGGER = LoggerFactory.getLogger(UsitOrderItemController.class);
	
    @Autowired
    private OrderItemService orderItemService;


    @Autowired
    private CommonService commonService;
    
    @Autowired
    private OrderService orderService;
    
    @Autowired
    private Environment env;
    
    
    /**
     * 주문아이템 조회
     * @param request
     * @param curPage
     * @param perPage
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/order-items/{orderItemId}", method=RequestMethod.GET)
    public ModelAndView list(HttpServletRequest request, @PathVariable String orderItemId) throws Exception{

        ModelAndView mav = new ModelAndView("jsonView");

        Map<String, Object> resultData = new HashMap<String, Object>();
        String resultCode = "0000";
        String resultMsg = "";

        boolean isAdmin = getSignedMember().hasRole(SignedMember.ROLE.USIT_ADMIN); // 관리자 권한 있나?
        boolean isPartner = getSignedMember().hasRole(SignedMember.ROLE.USIT_PARTNER); // 파트너 권한 있나?
        boolean isUser = getSignedMember().hasRole(SignedMember.ROLE.USIT_USER); // 일반사용자 인가?

        logger.debug("isAdmin:{}", isAdmin );
        logger.debug("isPartner:{}", isPartner );
        logger.debug("isUser:{}", isUser );

        UsitOrderItem orderItem = new UsitOrderItem();
        try {

            int iOrderItemId = Integer.parseInt(orderItemId);

            orderItem = orderItemService.getUsitOrderItem(iOrderItemId);

        }catch(FrameworkException e){
            logger.error("CommFrameworkException", e);
            resultCode = e.getMsgKey();
            resultMsg = e.getMsg();
        }catch(Exception e){
            logger.error("Exception", e);
            resultCode = "-9999";
            resultMsg = "처리중 오류가 발생하였습니다.";
        }

        mav.addObject("result_code", resultCode);
        mav.addObject("result_msg", resultMsg);
        mav.addObject("data", orderItem);

        return mav;
    }

    
    
    
    
    
    /**
     * 주문, 취소, 환불교환 아이템 조회
     * @param request
     * @param curPage
     * @param perPage
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/order-items/member", method=RequestMethod.GET)
    public ModelAndView memberList(HttpServletRequest request,@RequestParam("deliveryStatusCd") String deliveryStatusCd, @RequestParam("curPage") int curPage, @RequestParam("perPage") int perPage) throws Exception{

        ModelAndView mav = new ModelAndView("jsonView");

        String resultCode = "0000";
        String resultMsg = "";

        
        List<String> deliveryStatusCds = new ArrayList<String>();
        StringTokenizer stk = new StringTokenizer(deliveryStatusCd, ",");
      
        while(stk.hasMoreTokens()) {
        	deliveryStatusCds.add(stk.nextToken());
        }
        
        
        Page<UsitOrderItem> orderItems = null;
        try {
        	int memberId = getSignedMember().getMemberInfo().getMemberId();
            logger.debug("curPage:{}", curPage);
            logger.debug("perPage:{}", perPage);

            Pageable pageRequest = new PageRequest(curPage, perPage, Sort.Direction.DESC, "orderItemId");
            orderItems = orderItemService.getUsitOrderItemByMemberIdAndDeliveryStatusCdIn(memberId, deliveryStatusCds, pageRequest);

        }catch(FrameworkException e){
            logger.error("CommFrameworkException", e);
            resultCode = e.getMsgKey();
            resultMsg = e.getMsg();
        }catch(Exception e){
            logger.error("Exception", e);
            resultCode = "-9999";
            resultMsg = "처리중 오류가 발생하였습니다.";
        }

        mav.addObject("result_code", resultCode);
        mav.addObject("result_msg", resultMsg);
        mav.addObject("data", orderItems);

        return mav;
    }
    
    
    
    
    
    /**
     * 주문, 취소, 환불교환 아이템 통계 조회
     * @param request
     * @param curPage
     * @param perPage
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/order-items/member-statistics", method=RequestMethod.GET)
    public ModelAndView memberStatistics(HttpServletRequest request,@RequestParam("deliveryStatusCd") String deliveryStatusCd) throws Exception{

        ModelAndView mav = new ModelAndView("jsonView");

        String resultCode = "0000";
        String resultMsg = "";

        
        List<String> deliveryStatusCds = new ArrayList<String>();
        StringTokenizer stk = new StringTokenizer(deliveryStatusCd, ",");
        Map<String,Object> data = new HashMap<String, Object>();
                
        
        while(stk.hasMoreTokens()) {
        	deliveryStatusCds.add(stk.nextToken());
        }
        
        
        try {
        	int memberId = getSignedMember().getMemberInfo().getMemberId();
        	data = orderItemService.getCountByMemberIdAndDeliveryStatusCdIn(memberId, deliveryStatusCds);

        }catch(FrameworkException e){
            logger.error("CommFrameworkException", e);
            resultCode = e.getMsgKey();
            resultMsg = e.getMsg();
        }catch(Exception e){
            logger.error("Exception", e);
            resultCode = "-9999";
            resultMsg = "처리중 오류가 발생하였습니다.";
        }

        mav.addObject("result_code", resultCode);
        mav.addObject("result_msg", resultMsg);
        mav.addObject("data", data);

        return mav;
    }
    

    /**
    * 주문 아이템 수정
    * @param request
    * @param curPage
    * @param perPage
    * @return
    * @throws Exception
    */
   @SuppressWarnings("unchecked")
   @RequestMapping(value="/order-items/{orderItemId}", method=RequestMethod.PUT)
   public ModelAndView saveOrder(ComUiDTO dto, HttpServletRequest request, @PathVariable String orderItemId, @RequestBody UsitOrderItem paramUsitOrderItem,
			@RequestParam(name = "updateReturnRegDate",required = false) String updateReturnRegDate,@RequestParam(name ="updateReturnModDate",required = false) String updateReturnModDate) throws Exception {

		ModelAndView mav = new ModelAndView("jsonView");
		Map<String, Object> params = (Map<String, Object>) dto.getRequestBodyToObject();

		Map<String, Object> resultData = new HashMap<String, Object>();
		String resultCode = "0000";
		String resultMsg = "";
		UsitOrderItem result = new UsitOrderItem();
		try {

			/*
			//스윗트래커 반품접수 불가기간 체크 
			if("1401".equals(paramUsitOrderItem.getReturnStatusCd()) && "1301".equals(paramUsitOrderItem.getReturnObjectTypeCd())) {
				
				
				JSONObject checkResult = commonService.checkSweetTrackerReturnAcceptDay();
					if("Y".equals(checkResult.get("result"))) {

					Map<String, String> returnParams = new HashMap<String, String>();;
					returnParams.put("ordCde", paramUsitOrderItem.getOrderItemId().toString());
					returnParams.put("comCode", "04");
					returnParams.put("invoice",paramUsitOrderItem.getTrackingNumber());
					 //송화인
					returnParams.put("sndNme", paramUsitOrderItem.getReturnReceiverName());
					returnParams.put("sndZip", paramUsitOrderItem.getReturnReceiverPostcode());
					returnParams.put("sndAd1", paramUsitOrderItem.getReturnReceiverAddress());
					returnParams.put("sndAd2", paramUsitOrderItem.getReturnReceiverAddressDetail());
					returnParams.put("sndTel", paramUsitOrderItem.getReturnReceiverPhone());
		            //수화인
					returnParams.put("ownNme", "유짓");
					returnParams.put("ownZip", "15418");
					returnParams.put("ownAd1", "경기도 안산시 단원구 지원로 107");
					returnParams.put("ownAd2", "관리동 1층 109-2호");
					returnParams.put("ownTel", "070-8831-8025");
					returnParams.put("adMemo", paramUsitOrderItem.getReturnReceiverMessage());
					returnParams.put("wipGbn", "3");
					returnParams.put("mode", env.getProperty("running.system"));
					
					
					JSONObject returnResult = commonService.orderSweetTrackerReturn(returnParams);
					
					if("N".equals(returnResult.get("result"))) {
						LOGGER.warn("반품실패.");
						throw new FrameworkException("-1001", "반품접수에 실패하였습니다 관리자에게 문의해주세요."); // 오류 리턴 예시
					}
					
//				               "result":"Y",
//				               "ordCde":"HPLUS1234567890",
//				               "errCde":"",
//				               "errMsg":""
					
				}else {
					LOGGER.warn("반품접수 가능일이 아닙니다.");
					throw new FrameworkException("-1001", "반품접수 가능일이 아닙니다."); // 오류 리턴 예시
				}
			}
			
			*/
			
			//ORM은 객체가 달라도 DB와 같이 연동된다. 과거값은 변수로 저장할것
			UsitOrderItem pastOrderItem = orderItemService.getUsitOrderItem(paramUsitOrderItem.getOrderItemId());
			String pastTrackingNumber = pastOrderItem.getTrackingNumber();
			
			if (paramUsitOrderItem.getOrderItemId() > 0) {

				if ("true".equals(updateReturnRegDate)) {
					paramUsitOrderItem.setReturnRegDate(TimeUtil.getZonedDateTimeNow("Asia/Seoul"));
				} else {
					paramUsitOrderItem.setReturnRegDate(pastOrderItem.getReturnRegDate());
				}
				if ("true".equals(updateReturnModDate)) {
					paramUsitOrderItem.setReturnModDate(TimeUtil.getZonedDateTimeNow("Asia/Seoul"));
				} else {
					paramUsitOrderItem.setReturnModDate(pastOrderItem.getReturnRegDate());
				}
				
				//상품준비, 상품배송, 결제시간 업데이트
				if(paramUsitOrderItem.getDeliveryStatusCd().equals(UsitCodeConstants.DELIVERY_STATUS_CD_PAYMENT_COMPLETE)) {
				
					paramUsitOrderItem.setPaymentDate(TimeUtil.getZonedDateTimeNow("Asia/Seoul"));
				}else {
					paramUsitOrderItem.setPaymentDate(pastOrderItem.getPaymentDate());
				}
				if(paramUsitOrderItem.getDeliveryStatusCd().equals(UsitCodeConstants.DELIVERY_STATUS_CD_DELIVERY_STANDBY)) {
					paramUsitOrderItem.setOrderConfirmDate(TimeUtil.getZonedDateTimeNow("Asia/Seoul"));
				}else {
					paramUsitOrderItem.setOrderConfirmDate(paramUsitOrderItem.getOrderConfirmDate());
				}
					
				if(paramUsitOrderItem.getDeliveryStatusCd().equals(UsitCodeConstants.DELIVERY_STATUS_CD_DELIVERY_SEND)) {
					paramUsitOrderItem.setSendDate(TimeUtil.getZonedDateTimeNow("Asia/Seoul"));
				}else {
					paramUsitOrderItem.setSendDate(pastOrderItem.getSendDate());
				}
				
				
			}
			
			if(paramUsitOrderItem.getTrackingNumber() != null) {
				paramUsitOrderItem.setTrackingNumber(paramUsitOrderItem.getTrackingNumber().replaceAll("-", ""));
			}
			
			if(paramUsitOrderItem.getReturnTrackingNumber() != null) {
				paramUsitOrderItem.setReturnTrackingNumber(paramUsitOrderItem.getReturnTrackingNumber().replaceAll("-", ""));
			}

			result = orderItemService.setOrderItem(paramUsitOrderItem);

			
			// 배송 카카오알림톡 발송
			if (UsitCodeConstants.DELIVERY_STATUS_CD_DELIVERY_SEND.equals(paramUsitOrderItem.getDeliveryStatusCd()) && pastTrackingNumber == null
					&& paramUsitOrderItem.getTrackingNumber() != null) {

				UsitOrder order = orderService.getUsitOrderByOrderId(paramUsitOrderItem.getOrderId());
				// Member mem = memberService.getMemberByMemeberId(order.getMemberId());
//				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
//				String departureDate = TimeUtil.getZonedDateTimeNow("Asia/Seoul").format(formatter);

				boolean isSendedItem = false;
				int trackingNumberCount = 0;
				String trackingNumber = paramUsitOrderItem.getTrackingNumber();
				List<UsitOrderItem> it = order.getOrderItems();
				for (UsitOrderItem usitOrderItem : it) {
					if(trackingNumber.equals(usitOrderItem.getTrackingNumber())) {
						trackingNumberCount++;
					}
				}
				
				isSendedItem = trackingNumberCount > 1;
				if(!isSendedItem) {
				
				/**
				 * #{고객명} #{상품명} #{택배사명} #{송장번호}
				 */
				String variable[] = new String[4];

				variable[0] = order.getOrdererName();
				
				if(pastOrderItem.getQuantity() > 1) {
					variable[1] = String.valueOf(pastOrderItem.getProduct().getTitle() + " "+pastOrderItem.getQuantity());	
				}else {
					variable[1] = String.valueOf(pastOrderItem.getProduct().getTitle());	
				}				
				
				variable[2] = "";
				JSONObject company = commonService.getTrackerCompany();
		        JSONParser jsonParser = new JSONParser();
		        JSONObject jsonObj = (JSONObject) jsonParser.parse(company.toJSONString());
		        JSONArray deliveryArray = (JSONArray) jsonObj.get("Company");
		        int size = deliveryArray.size();
		        for(int i=0 ; i < size ; i++){
		            JSONObject tempObj = (JSONObject) deliveryArray.get(i);
		            if(tempObj.get("Code").equals(paramUsitOrderItem.getProduct().getDeliveryCompanyCd())) {
		            	variable[2]= String.valueOf(tempObj.get("Name"));
		            }
		        }
				
				variable[3] = paramUsitOrderItem.getTrackingNumber();

				int status = commonService.sendAlimtalk("U017", order.getOrdererPhone(), variable);
				LOGGER.info("kakaoStatus : " + status);
				}
				
			}

		} catch (FrameworkException e) {
			logger.error("CommFrameworkException", e);
			resultCode = e.getMsgKey();
			resultMsg = e.getMsg();
		} catch (Exception e) {
			logger.error("Exception", e);
			resultCode = "-9999";
			resultMsg = e.getCause().getCause().getCause().getCause().getMessage();
		}

		mav.addObject("result_code", resultCode);
		mav.addObject("result_msg", resultMsg);
		mav.addObject("data", result);

		return mav;
	}









   /**
    * 주문아이템 조회
    * @param request
    * @param curPage
    * @param perPage
    * @return
    * @throws Exception
    */
   @RequestMapping(value="/order-items/return-requests", method=RequestMethod.GET)
   public ModelAndView listReturnRequest(HttpServletRequest request, @RequestParam("curPage") int curPage, @RequestParam("perPage") int perPage) throws Exception{

       ModelAndView mav = new ModelAndView("jsonView");

       Pageable pageRequest = new PageRequest(curPage, perPage, Sort.Direction.DESC, "returnRegDate");

       Map<String, Object> resultData = new HashMap<String, Object>();
       String resultCode = "0000";
       String resultMsg = "";

       Page<UsitOrderItem> page = null;
       try {

           page = orderItemService.getUsitOrderItemReturnRequest(pageRequest);

       }catch(FrameworkException e){
           logger.error("CommFrameworkException", e);
           resultCode = e.getMsgKey();
           resultMsg = e.getMsg();
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
    * 주문아이템 조회
    * @param request
    * @param curPage
    * @param perPage
    * @return
    * @throws Exception
    */
   @RequestMapping(value="/order-items/return-requests/member", method=RequestMethod.GET)
   public ModelAndView listReturnRequestMember(HttpServletRequest request, @RequestParam("curPage") int curPage, @RequestParam("perPage") int perPage) throws Exception{

       ModelAndView mav = new ModelAndView("jsonView");

       Map<String, Object> resultData = new HashMap<String, Object>();

       Pageable pageRequest = new PageRequest(curPage, perPage, Sort.Direction.DESC, "orderItemId");
       String resultCode = "0000";
       String resultMsg = "";
       int memberId = getSignedMember().getMemberInfo().getMemberId();
       Page<UsitOrderItem> page = null;
       try {

           page = orderItemService.findAllByMemberIdAndReturnStatusCdIsNotNull(pageRequest, memberId);

       }catch(FrameworkException e){
           logger.error("CommFrameworkException", e);
           resultCode = e.getMsgKey();
           resultMsg = e.getMsg();
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
    * 판매자 판매아이템 조회
    * @param request
    * @param curPage
    * @param perPage
    * @return
    * @throws Exception
    */
   @RequestMapping(value="/order-items/seller-token", method=RequestMethod.GET)
   public ModelAndView sellerOrderItemList(@RequestParam("curPage") int curPage, @RequestParam("perPage") int perPage,
		   @RequestParam("periodCondition") String periodCondition,@RequestParam("startDate") String startDate,@RequestParam("endDate") String endDate,
		   @RequestParam(value="keywordCondition", defaultValue = "") String keywordCondition,@RequestParam(value="keyword", defaultValue = "") String keyword) throws Exception{
	   
//	   @RequestParam("curPage") int curPage, @RequestParam("perPage"), periodCondition, startDate, endDate, keywordCondition, keyword)
//	   periodCondition = paymentDate(결제일), orderConfirmDate(발주확인일), sendDate(상품발송일)
//	   keywordCondition = ordererName(구매자명), ordererPhone(구매자연락처), ordererEmail(구매자이메일), orderId(주문번호), orderItemId(주문상품번호), productId(상품번호), trackingNumber(송장번호)
	   
	   
       ModelAndView mav = new ModelAndView("jsonView");

       String resultCode = "0000";
       String resultMsg = "";

       
       Page<UsitOrderItem> orderItem = null;
       int memberId = getSignedMember().getMemberInfo().getMemberId();
       logger.debug("curPage:{}", curPage);
       logger.debug("perPage:{}", perPage);

       Pageable pageRequest = new PageRequest(curPage, perPage, Sort.Direction.DESC, "orderId");
       try {
           orderItem = orderItemService.getSellerOrderItemList(pageRequest,memberId,periodCondition,startDate,endDate,keywordCondition,keyword);
       }catch(FrameworkException e){
           logger.error("CommFrameworkException", e);
           resultCode = e.getMsgKey();
           resultMsg = e.getMsg();
       }catch(Exception e){
           logger.error("Exception", e);
           resultCode = "-9999";
           resultMsg = "처리중 오류가 발생하였습니다.";
       }

       mav.addObject("result_code", resultCode);
       mav.addObject("result_msg", resultMsg);
       mav.addObject("data", orderItem);

       return mav;
   }
   
   
   
   
   
   /**
    * 주문 취소 완료 콜백 (이니시스)
    * 아이템단위 취소를 위한 메소드
    * @param request
    * @param curPage
    * @param perPage
    * @return
    * @return
    * @throws Exception
    */
   @RequestMapping(value="/order-items/cancel", method=RequestMethod.POST)
   public ModelAndView cancelOrder(@RequestBody UsitOrderItem orderItem,@RequestParam("returnReasonCd") String returnReasonCd,@RequestParam("returnReasonText") String returnReasonText) throws Exception{




           ModelAndView mav = new ModelAndView("jsonView");
           String resultCode = "0000";
           String resultMsg = "";


           JSONObject result = null;

           try {

                result = orderItemService.updateOrderItemStatus(orderItem,returnReasonCd,returnReasonText);
           }catch(FrameworkException e){
               logger.error("CommFrameworkException", e);
               resultCode = e.getMsgKey();
               resultMsg = e.getMsg();
           }catch(Exception e){
               logger.error("Exception", e);
               resultCode = "-9999";
               resultMsg = "처리중 오류가 발생하였습니다.";
           }

           mav.addObject("result_code", resultCode);
           mav.addObject("result_msg", resultMsg);
           mav.addObject("data", result);


         return mav;



   }
   
   
   
   
   
   
   
   
   /**
    * 반품 접수결과 수신 콜백 (스위트래커)
    * @param request
    * @return
    * @throws Exception
    */
	@RequestMapping(value ="/order-items/return-requests/sweet-tracker", method = RequestMethod.POST)
	public ModelAndView saveOrderConfirm(HttpServletRequest request, ComUiDTO params) throws Exception {

		ModelAndView mav = new ModelAndView("jsonView");
		List <Map<String,Object>> data = new ArrayList<Map<String,Object>>();
//		Map<String, String> paramData = (Map<String, String>) params.getRequestBodyToObject();
		EtcUtil eu = new EtcUtil();
		JSONObject json =  eu.getJsonStringFromMap((Map<String, Object>) params.getRequestBodyToObject());
		

		 JSONParser jsonParser = new JSONParser();
         JSONObject jsonObj = (JSONObject) jsonParser.parse(json.toString());
         JSONArray resultArray = (JSONArray) jsonObj.get("result_data");

         System.out.println("=====Result=====");
         int size = resultArray.size();
         for(int i=0 ; i< size ; i++){
             JSONObject tempObj = (JSONObject) resultArray.get(i);
             Map<String, String> paramData  = eu.getMapFromJsonObject(tempObj);
             paramData.put("modDate",TimeUtil.getZonedDateTimeNow("Asia/Seoul").toString());
             // 처리시간
 			logger.debug("{}", paramData.get("trnTim"));
 			// 주문번호
 			logger.debug("{}", paramData.get("ordCde"));
 			// 운송장번호
 			logger.debug("{}", paramData.get("invoice"));
 			// 처리여부 (Y : 처리 / N : 미처리)
 			logger.debug("{}", paramData.get("trnYn"));
 			// 처리상태코드(처리상태코드표 참조)
 			logger.debug("{}", paramData.get("resCde"));
 			// 처리상태명(처리상태코드표 참조)
 			logger.debug("{}", paramData.get("resNme"));
 			// 택배사 코드
 			logger.debug("{}", paramData.get("comCode"));
 			// 스윗트래커 변환 주문번호
 			logger.debug("{}", paramData.get("stOrdCde"));
 			
 			
 			data.add(orderItemService.setOrderItemReturnSweetTracker(paramData));
         }
		

		
		mav.addObject("data", data);
	    
		return mav;

	}
   
   
   
// 택배사 배송상태 동기화 초 분 시 일 월 주(년)
//	@Scheduled(cron = "0 0/10 * * * ?")
//    @Scheduled(cron = "5 * * * * ?")
//   @Scheduled(cron = "0 0 12,17,22 * * ?")
//   @Scheduled(cron = "0 55 9 * * ?")
//   @Scheduled(cron = "0 45 20 * * ?")
//	@Scheduled(cron = "0 0 3,9,13 * * ?")
    public void updateDeliveryStatus() throws Exception{
    	if("real".equals(env.getProperty("running.system"))) {
	   logger.info("@@배송상태동기화 시작");
	   List<String> orderItemStatusCd = new ArrayList<String>();
	   orderItemStatusCd.add("1203");
	   orderItemStatusCd.add("1206");
	   List<UsitOrderItem> checkList = orderItemService.getUsitOrderItemByDeliveryStatusCdIn(orderItemStatusCd);
    	  
    	for (Iterator<UsitOrderItem> iterator = checkList.iterator(); iterator.hasNext();) {
    		UsitOrderItem usitOrderItem = (UsitOrderItem) iterator.next();
			JSONObject result = commonService.checkTracker(usitOrderItem.getTrackingNumber(),usitOrderItem.getProduct().getDeliveryCompanyCd());
			if("Y".equals(result.get("completeYN"))) {
				if("1203".equals(usitOrderItem.getDeliveryStatusCd())){
					logger.info("@@equals 1203: ");
					usitOrderItem.setDeliveryStatusCd("1204");
				}else if("1206".equals(usitOrderItem.getDeliveryStatusCd())){
					logger.info("@@equals 1206: ");
					usitOrderItem.setDeliveryStatusCd("1207");
				}else {
					//추후변경분
				}
				logger.info("@@배송상태동기화 배송번호: "+ usitOrderItem.getTrackingNumber());
				orderItemService.setOrderItemTracker(usitOrderItem);
				logger.info("@@save ");
				logger.info("@@status: " + usitOrderItem.getDeliveryStatusCd());
				Thread.sleep(1000);
			}
		}
    	
    
    	}
    }
    
    
    
    
    
    
    
 // 택배사 반송상태 동기화 초 분 시 일 월 주(년)
// 	@Scheduled(cron = "0 10 3,9,13 * * ?")
     public void updateReturnDeliveryStatus() throws Exception{
     	if("real".equals(env.getProperty("running.system"))) {
     	logger.info("@@반송상태동기화 시작");
     	List<UsitOrderItem> returnCheckList = orderItemService.getUsitOrderItemByReturnStatusCd("1402");
     	  
     	for (Iterator<UsitOrderItem> iterator = returnCheckList.iterator(); iterator.hasNext();) {
 			UsitOrderItem usitOrderItem = (UsitOrderItem) iterator.next();
 			JSONObject result = commonService.checkTracker(usitOrderItem.getReturnTrackingNumber(), usitOrderItem.getProduct().getDeliveryCompanyCd());
 			if("Y".equals(result.get("completeYN"))) {
 					usitOrderItem.setReturnStatusCd("1403");
 					
 				logger.info("@@반송상태동기화 배송번호: "+ usitOrderItem.getReturnTrackingNumber());
 				orderItemService.setOrderItemReturnTracker(usitOrderItem);
 				logger.info("@@save ");
 				logger.info("@@status: " +usitOrderItem.getReturnTrackingNumber() +":"+ usitOrderItem.getReturnStatusCd());
 				Thread.sleep(1000);
 			}
 		}
     	
     	}
     }
    
    
     
     
   //카카오택 테스트 초 분 시 일 월 주(년)
// 	@Scheduled(cron = "0 22 22 * * ?")
//    public void test() throws Exception{
// 		
// 		String  phone [] = {"01087736957","01091798025","01036628388","01052356112"};
// 		//ORM은 객체가 달라도 DB와 같이 연동된다. 과거값은 변수로 저장할것
// 		for(int j =0 ;j < phone.length;j++) {
// 		
// 		UsitOrderItem pastOrderItem = orderItemService.getUsitOrderItem(105576);
// 		String pastTrackingNumber = pastOrderItem.getTrackingNumber();
// 		
//
// 		try {
// 		// 배송 카카오알림톡 발송
// 		if (UsitCodeConstants.DELIVERY_STATUS_CD_DELIVERY_SEND.equals(pastOrderItem.getDeliveryStatusCd())) {
//
// 			UsitOrder order = orderService.getUsitOrderByOrderId(pastOrderItem.getOrderId());
// 			// Member mem = memberService.getMemberByMemeberId(order.getMemberId());
//// 			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
//// 			String departureDate = TimeUtil.getZonedDateTimeNow("Asia/Seoul").format(formatter);
//
// 			boolean isSendedItem = false;
// 			int trackingNumberCount = 0;
// 			String trackingNumber = pastOrderItem.getTrackingNumber();
// 			List<UsitOrderItem> it = order.getOrderItems();
// 			for (UsitOrderItem usitOrderItem : it) {
// 				if(trackingNumber.equals(usitOrderItem.getTrackingNumber())) {
// 					trackingNumberCount++;
// 				}
// 			}
// 			
// 			isSendedItem = trackingNumberCount > 1;
// 			if(!isSendedItem) {
// 			
// 			/**
// 			 * #{고객명} #{상품명} #{택배사명} #{송장번호}
// 			 */
// 			String variable[] = new String[4];
//
// 			variable[0] = order.getOrdererName();
// 			
// 			if(pastOrderItem.getQuantity() > 1) {
// 				variable[1] = String.valueOf(pastOrderItem.getProduct().getTitle() + " "+pastOrderItem.getQuantity());	
// 			}else {
// 				variable[1] = String.valueOf(pastOrderItem.getProduct().getTitle());	
// 			}				
// 			
// 			variable[2] = "";
// 			JSONObject company = commonService.getTrackerCompany();
// 	        JSONParser jsonParser = new JSONParser();
// 	        JSONObject jsonObj = (JSONObject) jsonParser.parse(company.toJSONString());
// 	        JSONArray deliveryArray = (JSONArray) jsonObj.get("Company");
// 	        int size = deliveryArray.size();
// 	        for(int i=0 ; i < size ; i++){
// 	            JSONObject tempObj = (JSONObject) deliveryArray.get(i);
// 	            if(tempObj.get("Code").equals(pastOrderItem.getProduct().getDeliveryCompanyCd())) {
// 	            	variable[2]= String.valueOf(tempObj.get("Name"));
// 	            }
// 	        }
// 			
// 			variable[3] = pastOrderItem.getTrackingNumber();
//
// 			int status = commonService.sendAlimtalk("U017", phone[j], variable);
// 			LOGGER.info("kakaoStatus : " + status);
// 			}
// 			
// 		}
//
// 	} catch (FrameworkException e) {
// 		logger.error("CommFrameworkException", e);
// 	} catch (Exception e) {
// 		logger.error("Exception", e);
// 	}
//
// 		}
// 		
// 	}
     
  
     
}
    


    

